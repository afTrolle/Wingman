package dev.trolle.wingman.user

import dev.trolle.wingman.ai.AI
import dev.trolle.wingman.user.model.Match
import dev.trolle.wingman.user.tinder.Tinder
import dev.trolle.wingman.user.tinder.model.MatchesResponse
import dev.trolle.wingman.user.tinder.model.ProfileResponse
import dev.trolle.wingman.user.tinder.profileString
import io.github.aakira.napier.Napier
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.runBlocking
import kotlinx.datetime.Clock
import kotlinx.datetime.TimeZone
import kotlinx.datetime.yearsUntil
import dev.trolle.wingman.user.tinder.model.Match as TinderMatch

interface User {
    val isUserSignedIn: Boolean
    suspend fun signInRequestOneTimePassword(phoneNumber: String)
    suspend fun signInOneTimePassword(oneTimePassword: String, phoneNumber: String)
    suspend fun getMatches(): List<Match>
    val matchesFlow: Flow<List<UiMatch>>
}

internal fun user(
    ai: AI,
    tinder: Tinder,
    database: UserDatabase,
) = object : User {

    override suspend fun signInRequestOneTimePassword(phoneNumber: String) {
        tinder.otp(phoneNumber)
    }

    override suspend fun signInOneTimePassword(oneTimePassword: String, phoneNumber: String) {
        tinder.refreshToken(
            oneTimePassword = oneTimePassword,
            phoneNumber = phoneNumber,
        )
    }

    override val isUserSignedIn: Boolean
        get() = runBlocking {
            tinder.isSignedIn()
        }

    // TODO change this to a flow so we can stream data to ui
    override suspend fun getMatches(): List<Match> {
        val myProfile = tinder.myProfile()
        val myPublicProfile = tinder.profile(myProfile.id).results ?: error("Error fetch profile")
        Napier.d { "fetched profile" }
        // TODO add paging
        val numberOfMatches = 10
        val response = tinder.matches(numberOfMatches)
        Napier.d { "fetched matches" }
        val matches = response.matchesWithNoMessages()

        return matches.map { match ->
            val personId = match.person.id
            Napier.d { "Handling ${match.person.name}" }
            val matchProfile = tinder.profile(personId).results
                ?: error("Error fetch profile")

            val suggestion = getSuggestionForOpener(personId, myPublicProfile, matchProfile)

            Match(
                name = match.person.name ?: "",
                age = match.person.age()?.toString() ?: "",
                imageUrl = match.person.photos.first().url!!,
                opener = suggestion,
            )
        }
    }

    private suspend fun getSuggestionForOpener(
        personId: String,
        myPublicProfile: ProfileResponse.Profile,
        matchProfile: ProfileResponse.Profile,
    ) = database.suggestionsFlow.value.getOrElse(personId) {
        ai.fetchSuggestion(
            myPublicProfile,
            matchProfile,
        )?.also { suggestion ->
            database.addSuggestion(personId, suggestion)
        } ?: ""
    }

    override val matchesFlow: Flow<List<UiMatch>> =
        flow {
            val myProfile = tinder.myProfile()
            val myPublicProfile =
                tinder.profile(myProfile.id).results ?: error("Error fetch profile")
            val numberOfMatches = 10 // TODO Add paging
            val response = tinder.matches(numberOfMatches)
            val matches = response.matchesWithNoMessages()

            emit(matches.toMatchInfo.toBasicMatch)

            val matchesWithSuggestion = matches.map { match ->
                val personId = match.person.id
                val matchProfile = tinder.profile(personId).results ?: error("Error fetch profile")
                val suggestion = getSuggestionForOpener(personId, myPublicProfile, matchProfile)
                UiMatch.SuggestionMatch(
                    info = match.toMatchInfo,
                    suggestion = suggestion,
                )
            }

            emit(matchesWithSuggestion)
        }

    private val List<TinderMatch>.toMatchInfo
        get() = map { it.toMatchInfo }
    private val TinderMatch.toMatchInfo
        get() = person.run { MatchInfo(name ?: "", age()?.toString() ?: "", photos.first().url!!) }

    private val List<MatchInfo>.toBasicMatch
        get() = map { UiMatch.BasicMatch(it) }
}

data class MatchInfo(
    val name: String,
    val age: String,
    val imageUrl: String,
)

sealed class UiMatch {
    abstract val info : MatchInfo
    data class BasicMatch(
        override val info: MatchInfo,
    ) : UiMatch()

    data class SuggestionMatch(
        override val info: MatchInfo,
        val suggestion: String,
    ) : UiMatch()

}

fun MatchesResponse.matchesWithNoMessages(): List<TinderMatch> = data?.matches
    ?.filter { it.messageCount == 0 && it.messages.isEmpty() } ?: emptyList()

suspend fun AI.fetchSuggestion(
    myPublicProfile: ProfileResponse.Profile,
    matchProfile: ProfileResponse.Profile,
): String? {
    val context = listOfNotNull(
        buildString {
            appendLine("You are the following person")
            append(myPublicProfile.profileString())
        },
        buildString {
            appendLine("Tinder online dating match")
            append(matchProfile.profileString())
        },
    )

    val text =
        "Write an simple, short and flirty opening message that that will catch the attention of ${matchProfile.name}. " +
                "Make sure that the message contains a fun tone, compliments the match and contains a call to action or a question than needs some reflection in the following language=en"
    return prompt(
        context,
        listOf(text),
    )
}

fun TinderMatch.Person.age() = birthDate?.yearsUntil(Clock.System.now(), TimeZone.UTC)
