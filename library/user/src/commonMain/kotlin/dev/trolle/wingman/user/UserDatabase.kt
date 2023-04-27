package dev.trolle.wingman.user

import dev.trolle.wingman.db.Database
import dev.trolle.wingman.db.get
import dev.trolle.wingman.db.getBlocking
import dev.trolle.wingman.db.set
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.update
import kotlinx.datetime.Clock
import kotlinx.datetime.Instant
import kotlinx.serialization.Serializable

internal class UserDatabase(
    private val database: Database,
) {
    private val scope = CoroutineScope(Dispatchers.Default + SupervisorJob())

    fun addSuggestion(personId: String, suggestion: String) {
        suggestionsFlow.update {
            it.plus(personId to suggestion)
        }
    }

    val suggestionsFlow = MutableStateFlow<Map<String, String>>(
        database.getBlocking(suggestionsKey) ?: emptyMap(),
    )

    init {
        // Updates handled like side effect
        suggestionsFlow.onEach {
            database.set(suggestionsKey, it)
        }.launchIn(scope)
    }

    companion object {
        private const val suggestionsKey = "suggestion"
    }
}

