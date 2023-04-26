package dev.trolle.wingman.user

import dev.trolle.wingman.db.Database
import dev.trolle.wingman.db.get
import dev.trolle.wingman.db.getBlocking
import dev.trolle.wingman.db.set
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.datetime.Clock
import kotlinx.datetime.Instant
import kotlinx.serialization.Serializable

internal class UserDatabase(
    private val database: Database,
) {
    fun addSuggestion(personId: String, suggestion: String) {
        // TODO fix this
    }

    suspend fun sessionData(): SessionData? = database.get(sessionKey)
    suspend fun setSessionData(sessionData: SessionData?) {
        database.set(sessionKey, sessionData)
    }

    val suggestionsFlow = MutableStateFlow<Map<String, String>>(
        database.getBlocking(suggestionsKey) ?: emptyMap(),
    )

    companion object {
        private const val suggestionsKey = "suggestion"
        private const val sessionKey = "session"
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
