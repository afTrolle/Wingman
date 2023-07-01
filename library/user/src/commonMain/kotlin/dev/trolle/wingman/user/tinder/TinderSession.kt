package dev.trolle.wingman.user.tinder

import dev.trolle.wingman.db.Database
import dev.trolle.wingman.db.set
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.sync.withLock
import kotlinx.serialization.builtins.serializer
import kotlin.random.Random

class TinderSession(private val database: Database) {

    private val sessionMutex = Mutex()

    val persistenceId = runBlocking {
        database.getOrSet(
            persistenceKey,
            String.serializer(),
        ) { generateTinderPersistenceKey() }
    }

    suspend fun updateSession(update: (SessionData) -> SessionData) = withSession { session ->
        val updatedSession = update(session)
        database.set(key, updatedSession)
    }

    suspend fun <T> withSession(action: suspend (SessionData) -> T) = sessionMutex.withLock {
        val session = database.getOrSet(key, SessionData.serializer()) { SessionData() }
        action(session)
    }

    suspend fun <T> withAccessToken(action: suspend (token: String) -> T) : T = withSession {
       val token =  it.accessToken?.token ?: error("No access token")
        action(token)
    }

    suspend fun removeAccessToken() = updateSession {
        it.copy(accessToken = null)
    }

    suspend fun signOut() = updateSession {
        it.copy(accessToken = null, refreshToken = null, phoneNumber = null)
    }

    suspend fun isSignedIn() = withSession { it.isSignedIn() }

    companion object {
        private const val key = "tinderSessionKey"
        private const val persistenceKey = "tinderPersistenceIdKey"

        // 64-bit value as hex string - should retain between app restarts (guess refresh token is locked with this)
        fun generateTinderPersistenceKey() = Random.nextLong().toString(16)
    }

}

fun SessionData.isSignedIn(): Boolean = accessToken != null && refreshToken != null