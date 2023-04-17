package dev.trolle.af.wingman.repository

import androidx.compose.ui.text.intl.Locale
import dev.trolle.af.wingman.ext.runCatchingCancelable
import dev.trolle.af.wingman.screen.Match
import dev.trolle.af.wingman.service.OpenAIService
import dev.trolle.af.wingman.service.PersistenceService
import dev.trolle.af.wingman.service.tinder.model.MatchesResponse
import dev.trolle.af.wingman.service.tinder.model.ProfileResponse
import dev.trolle.af.wingman.service.tinder.profileString
import dev.trolle.af.wingman.service.tinder.TinderService
import io.github.aakira.napier.Napier
import io.ktor.utils.io.errors.IOException
import kotlinx.coroutines.delay
import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.sync.withLock
import kotlinx.datetime.Clock
import kotlinx.datetime.Instant
import kotlinx.datetime.TimeZone
import kotlinx.datetime.yearsUntil
import kotlinx.serialization.Serializable
import dev.trolle.af.wingman.service.tinder.model.Match as TinderMatch

interface UserRepository {
    suspend fun signInRequestOneTimePassword(phoneNumber: String)
    suspend fun signInOneTimePassword(oneTimePassword: String, phoneNumber: String)
    suspend fun isUserSignedIn(): Boolean
    suspend fun getMatches(): List<Match>
}

fun userRepository(
    openAIService: OpenAIService,
    tinderService: TinderService,
    persistenceService: PersistenceService,
) = object : UserRepository {

    override suspend fun signInRequestOneTimePassword(phoneNumber: String) {
        tinderService.otp(phoneNumber)
    }

    override suspend fun signInOneTimePassword(oneTimePassword: String, phoneNumber: String) {
        val response = tinderService.refreshToken(
            oneTimePassword = oneTimePassword, phoneNumber = phoneNumber
        )

        val accessResponse = tinderService.accessToken(response.data.refreshToken)
        val refreshToken = accessResponse.data?.refreshToken ?: response.data.refreshToken
        setSession(
            phoneNumber = phoneNumber,
            refreshToken = refreshToken,
            SessionData.AccessToken(token = accessResponse.data!!.apiToken)
        )

        // User is seen as signed in after this point
        onSignIn()
    }

    private fun onSignIn() {

    }

    override suspend fun isUserSignedIn(): Boolean {
        return persistenceService.sessionData() != null
    }

    // TODO change this to a flow so we can stream data to ui
    override suspend fun getMatches(): List<Match> {
        val accessToken = getAccessToken().token
        // TODO could save this call during SignIn
        val myProfile = tinderService.myProfile(accessToken)
        val myPublicProfile =
            tinderService.profile(accessToken, myProfile.id).results ?: error("Error fetch profile")
        Napier.d { "fetched profile" }
        // TODO add paging
        val numberOfMatches = 10
        val response = tinderService.matches(accessToken, numberOfMatches)
        Napier.d { "fetched matches" }
        val matches = response.matchesWithNoMessages()

        return matches.map { match ->
            val personId = match.person.id
            Napier.d { "Handling ${match.person.name}" }
            val matchProfile = tinderService.profile(accessToken, personId).results
                ?: error("Error fetch profile")

            val suggestion = getSuggestionForOpener(personId, myPublicProfile, matchProfile)

            Match(
                name = match.person.name ?: "",
                age = match.person.age()?.toString() ?: "",
                imageUrl = match.person.photos.first().url!!,
                opener = suggestion
            )
        }
    }

    private suspend fun getSuggestionForOpener(
        personId: String,
        myPublicProfile: ProfileResponse.Profile,
        matchProfile: ProfileResponse.Profile
    ) = persistenceService.suggestions.value[personId]
        ?: openAIService.fetchSuggestion(
            myPublicProfile,
            matchProfile
        )?.also { suggestion ->
            persistenceService.addSuggestion(personId, suggestion)
        } ?: ""


    /*
    Utility
     */

    // synchronize sessions data changes!
    private val sessionLock = Mutex()
    private suspend fun setSession(
        phoneNumber: String,
        refreshToken: String,
        accessToken: SessionData.AccessToken
    ) =
        sessionLock.withLock {
            persistenceService.setSessionData(
                SessionData(
                    refreshToken = refreshToken,
                    phoneNumber = phoneNumber,
                    accessToken = accessToken
                )
            )
        }

    private suspend fun getAccessToken(): SessionData.AccessToken = sessionLock.withLock {
        val sessionData = persistenceService.sessionData() ?: error("Not signed in")
        val accessToken = sessionData.accessToken
        if (accessToken != null) {
            sessionData.accessToken
        } else {
            runCatchingCancelable {
                val response = tinderService.accessToken(sessionData.refreshToken)
                Napier.d { "fetched accessToken" }
                val refreshToken = response.data?.refreshToken ?: sessionData.refreshToken
                val apiToken = response.data?.apiToken ?: error("No Access token received")
                val updatedAccessToken = SessionData.AccessToken(apiToken)
                val updatedSession = sessionData.copy(
                    accessToken = updatedAccessToken,
                    refreshToken = refreshToken,
                )
                persistenceService.setSessionData(updatedSession)
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
        persistenceService.setSessionData(null)
    }

    private suspend fun removeAccessToken() = sessionLock.withLock {
        val session = persistenceService.sessionData()?.copy(accessToken = null)
        persistenceService.setSessionData(session)
    }
}

fun MatchesResponse.matchesWithNoMessages(): List<TinderMatch> = data?.matches
    ?.filter { it.messageCount == 0 && it.messages.isEmpty() } ?: emptyList()

suspend fun OpenAIService.fetchSuggestion(
    myPublicProfile: ProfileResponse.Profile,
    matchProfile: ProfileResponse.Profile
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

    val language = Locale.current.language
    val text =
        "Write an simple, short and flirty opening message that that will catch the attention of ${matchProfile.name}. " +
                "Make sure that the message contains a fun tone, compliments the match and contains a call to action or a question than needs some reflection in the following language=$language"
    return prompt(
        context,
        listOf(text)
    )?.message?.content
}

fun TinderMatch.Person.age() = birthDate?.yearsUntil(Clock.System.now(), TimeZone.UTC)

@Serializable
data class SessionData(
    val accessToken: AccessToken? = null,
    val refreshToken: String,
    val phoneNumber: String,
) {
    @Serializable
    data class AccessToken(
        val token: String,
        val timestamp: Instant = Clock.System.now(),
    )
}
