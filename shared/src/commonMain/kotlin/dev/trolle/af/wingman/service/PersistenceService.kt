package dev.trolle.af.wingman.service

import com.russhwolf.settings.coroutines.FlowSettings
import dev.trolle.af.wingman.repository.SessionData
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.runBlocking
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json

internal interface PersistenceService {
    val sessionFlow: Flow<SessionData?>
    val suggestionsFlow: StateFlow<Map<String, String>>
    suspend fun sessionData(): SessionData?
    suspend fun setSessionData(sessionData: SessionData?)
    fun addSuggestion(matchId: String, suggestion: String)
}

// Consider using realm or sqlDelight,
internal fun persistenceService(
    settings: FlowSettings,
    json: Json,
) = object : PersistenceService {

    private val scope = CoroutineScope(Dispatchers.Default + SupervisorJob())

    // Need to use locks for session and not mutable state
    // cause update function can be called multiple times until it succeeds
    private val sessionDataKey = "sessionData"
    private val suggestionStoreKey = "suggestionStore"

    override val sessionFlow = settingFlow<SessionData>(sessionDataKey)

    override val suggestionsFlow = MutableStateFlow<Map<String, String>>(
        getValueBlocking(suggestionStoreKey) ?: emptyMap(),
    )

    init {
        // TODO handle error if it fails to store suggestion
        suggestionsFlow.onEach {
            updateSetting(suggestionStoreKey, it)
        }.launchIn(scope)
    }

    override suspend fun sessionData(): SessionData? = sessionFlow.firstOrNull()

    override suspend fun setSessionData(sessionData: SessionData?) =
        updateSetting(sessionDataKey, sessionData)

    override fun addSuggestion(matchId: String, suggestion: String) {
        suggestionsFlow.update { it.plus(matchId to suggestion) }
    }

    /*
    Utility
     */
    private suspend inline fun <reified T> updateSetting(key: String, value: T) {
        val encodedValue = json.encodeToString(value)
        settings.putString(key, encodedValue)
    }

    private inline fun <reified T> settingFlow(key: String): Flow<T?> =
        settings.getStringOrNullFlow(key).map {
            it?.let { json.decodeFromString<T>(it) }
        }

    private suspend inline fun <reified T> getValue(key: String): T? =
        settings.getStringOrNull(key)?.let { json.decodeFromString<T>(it) }

    private suspend inline fun <reified T> getValueDefault(key: String, default: () -> T): T =
        getValue(key) ?: default()

    private inline fun <reified T> getValueBlocking(key: String): T? =
        runBlocking { getValue(key) }
}
