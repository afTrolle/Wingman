package dev.trolle.wingman.user.tinder

import dev.trolle.wingman.common.ext.runCatchingCancelable
import dev.trolle.wingman.db.Database
import dev.trolle.wingman.db.get
import dev.trolle.wingman.db.set
import dev.trolle.wingman.user.tinder.model.AccessTokenResponse
import io.ktor.client.plugins.ClientRequestException
import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.sync.withLock

class TinderSession(
    private val database: Database,
    private val accessToken: suspend (refreshToken: String) -> AccessTokenResponse,
) {
    private val sessionMutex = Mutex()

    suspend fun setSession(sessionData: SessionData) = sessionMutex.withLock {
        database.set(key, sessionData)
    }

    suspend fun getAccessToken(): SessionData.AccessToken = sessionMutex.withLock {
        val session = database.get<SessionData>(key) ?: error("Not signed in")
        return session.accessToken
            ?: runCatchingCancelable {
                val response = accessToken(session.refreshToken).data ?: throw MissingDataException
                val accessToken = response.apiToken

                database.set<SessionData?>(key, null)
                val updatedSession = SessionData(
                    refreshToken = session.refreshToken,
                    phoneNumber = session.phoneNumber,
                    accessToken = SessionData.AccessToken(accessToken),
                ).also {
                    database.set(key, it)
                }
                updatedSession.accessToken ?: throw MissingDataException
            }.onFailure {
                // Side effect
                when (it) {
                    is ClientRequestException -> signOut()
                    is MissingDataException -> signOut()
                }
            }.getOrThrow()
    }

    suspend fun removeAccessToken() = sessionMutex.withLock {
        val session = database.get<SessionData>(key) ?: error("Not signed in")
        database.set(key, session.copy(accessToken = null))
    }

    suspend fun signOut() = sessionMutex.withLock {
        database.set<SessionData?>(key, null)
    }

    suspend inline fun <T> withSession(call: (token: String) -> T): T =
        runCatchingCancelable {
            val accessToken = getAccessToken()
            call(accessToken.token)
        }.recoverCatching {
            when (it) {
                is ClientRequestException -> {
                    removeAccessToken()
                    val accessToken = getAccessToken()
                    call(accessToken.token)
                }

                else -> throw it
            }
        }.onFailure {
            when (it) {
                is ClientRequestException -> signOut()
                else -> throw it
            }
        }.getOrThrow()

    suspend fun isSignedIn() = database.get<SessionData>(key) != null

    companion object {
        private const val key = "tinderSessionKey"
    }

}