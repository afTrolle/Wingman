package dev.trolle.af.wingman.repository

import dev.trolle.af.wingman.service.PersistenceService
import dev.trolle.app.service.tinder.TinderService
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.datetime.Clock
import kotlinx.datetime.Instant
import kotlinx.serialization.Serializable

interface UserRepository {
    suspend fun signInRequestOneTimePassword(phoneNumber: String)
    suspend fun signInOneTimePassword(oneTimePassword: String, phoneNumber: String)
    suspend fun isUserSignedIn(): Boolean
}

fun userRepository(
    tinderService: TinderService,
    persistenceService: PersistenceService,
) = object : UserRepository {

    override suspend fun signInRequestOneTimePassword(phoneNumber: String) {
        tinderService.otp(phoneNumber)
    }

    override suspend fun signInOneTimePassword(oneTimePassword: String, phoneNumber: String) {
        val response = tinderService.refreshToken(
            oneTimePassword = oneTimePassword,
            phoneNumber = phoneNumber
        )
        val (refreshToken, accessToken) = fetchAccessToken(response.data.refreshToken)
        val sessionData = SessionData(
            accessToken = accessToken,
            refreshToken = refreshToken,
            phoneNumber = phoneNumber
        )
        persistenceService.setSessionData(sessionData)
    }

    private suspend fun fetchAccessToken(refreshToken: String): Pair<String, SessionData.AccessToken> {
        val response = tinderService.accessToken(refreshToken)
        val updatedRefreshToken = response.data?.refreshToken ?: refreshToken
        val accessToken = response.data?.apiToken ?: error("No api token received")
        return updatedRefreshToken to SessionData.AccessToken(accessToken)
    }

    override suspend fun isUserSignedIn(): Boolean {
        return persistenceService.getSessionData().firstOrNull() != null
    }
}

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
