package dev.trolle.wingman.user

import dev.trolle.wingman.ai.AI
import dev.trolle.wingman.common.ext.runCatchingCancelable
import dev.trolle.wingman.user.model.Match
import dev.trolle.wingman.user.tinder.Tinder
import dev.trolle.wingman.user.tinder.model.MatchesResponse
import dev.trolle.wingman.user.tinder.model.ProfileResponse
import dev.trolle.wingman.user.tinder.profileString
import io.github.aakira.napier.Napier
import io.ktor.utils.io.errors.IOException
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.sync.withLock
import kotlinx.datetime.Clock
import kotlinx.datetime.TimeZone
import kotlinx.datetime.yearsUntil
import dev.trolle.wingman.user.tinder.model.Match as TinderMatch

interface User {
    val isUserSignedIn: Boolean
    suspend fun signInRequestOneTimePassword(phoneNumber: String)
    suspend fun signInOneTimePassword(oneTimePassword: String, phoneNumber: String)
    suspend fun getMatches(): List<Match>
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
        val response = tinder.refreshToken(
            oneTimePassword = oneTimePassword,
            phoneNumber = phoneNumber,
        )

        val accessResponse = tinder.accessToken(response.data.refreshToken)
        val refreshToken = accessResponse.data?.refreshToken ?: response.data.refreshToken
        setSession(
            phoneNumber = phoneNumber,
            refreshToken = refreshToken,
            SessionData.AccessToken(token = accessResponse.data!!.apiToken),
        )

        // User is seen as signed in after this point
        onSignIn()
    }

    private fun onSignIn() {
    }

    override val isUserSignedIn: Boolean
        get() = runBlocking {
            database.sessionData() != null
        }

    // TODO change this to a flow so we can stream data to ui
    override suspend fun getMatches(): List<Match> {
        val accessToken = getAccessToken().token
        // TODO could save this call during SignIn
        val myProfile = tinder.myProfile(accessToken)
        val myPublicProfile =
            tinder.profile(accessToken, myProfile.id).results ?: error("Error fetch profile")
        Napier.d { "fetched profile" }
        // TODO add paging
        val numberOfMatches = 10
        val response = tinder.matches(accessToken, numberOfMatches)
        Napier.d { "fetched matches" }
        val matches = response.matchesWithNoMessages()

        return matches.map { match ->
            val personId = match.person.id
            Napier.d { "Handling ${match.person.name}" }
            val matchProfile = tinder.profile(accessToken, personId).results
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

    /*
    Utility
     */

    // synchronize sessions data changes!
    private val sessionLock = Mutex()
    private suspend fun setSession(
        phoneNumber: String,
        refreshToken: String,
        accessToken: SessionData.AccessToken,
    ) =
        sessionLock.withLock {
            database.setSessionData(
                SessionData(
                    refreshToken = refreshToken,
                    phoneNumber = phoneNumber,
                    accessToken = accessToken,
                ),
            )
        }

    private suspend fun getAccessToken(): SessionData.AccessToken = sessionLock.withLock {
        val sessionData = database.sessionData() ?: error("Not signed in")
        val accessToken = sessionData.accessToken
        if (accessToken != null) {
            sessionData.accessToken
        } else {
            runCatchingCancelable {
                val response = tinder.accessToken(sessionData.refreshToken)
                Napier.d { "fetched accessToken" }
                val refreshToken = response.data?.refreshToken ?: sessionData.refreshToken
                val apiToken = response.data?.apiToken ?: error("No Access token received")
                val updatedAccessToken = SessionData.AccessToken(apiToken)
                val updatedSession = sessionData.copy(
                    accessToken = updatedAccessToken,
                    refreshToken = refreshToken,
                )
                database.setSessionData(updatedSession)
                updatedAccessToken
            }.onFailure {
                // Failed to get access token
                if (it !is IOException) {
                    Napier.e("fetch AccessToken", it)
                    signOut()
                }
            }.getOrThrow()
        }
    }

    private suspend fun signOut() = sessionLock.withLock {
        database.setSessionData(null)
    }

    private suspend fun removeAccessToken() = sessionLock.withLock {
        val session = database.sessionData()?.copy(accessToken = null)
        database.setSessionData(session)
    }
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
