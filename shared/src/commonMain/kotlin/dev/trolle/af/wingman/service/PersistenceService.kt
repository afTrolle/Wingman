package dev.trolle.af.wingman.service

import com.russhwolf.settings.coroutines.FlowSettings
import dev.trolle.af.wingman.repository.SessionData
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.IO
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
    suspend fun setSessionData(sessionData: SessionData?)

    val sessionDataFlow: Flow<SessionData?>
    suspend fun sessionData(): SessionData?
    val suggestions: StateFlow<Map<String, String>>
    fun addSuggestion(matchId: String, suggestion: String)
}


const val sessionDataKey = "sessionData"
const val suggestionStoreKey = "suggestionStore"

// Consider using realm or sqlDelight,
internal fun persistenceService(
    settings: FlowSettings,
    json: Json
) = object : PersistenceService {

    // TODO add error handling for failure to write to persistence "out of disk space for example"
    private val scope = CoroutineScope(Dispatchers.IO + SupervisorJob())

    override suspend fun setSessionData(sessionData: SessionData?) =
        updateSetting(sessionDataKey, sessionData)

    // Need to use locks for session and not mutable state
    // cause update function can be called multiple times until it succeeds
    override val sessionDataFlow = settingFlow<SessionData>(sessionDataKey)
    override suspend fun sessionData(): SessionData? = sessionDataFlow.firstOrNull()

    override val suggestions = MutableStateFlow<Map<String, String>>(
        getValueBlocking<Map<String, String>?>(suggestionStoreKey) ?: emptyMap()
    )

    override fun addSuggestion(matchId: String, suggestion: String) {
        suggestions.update { it.plus(matchId to suggestion) }
    }

    init {
        // on each change update the persistence
        suggestions.onEach {
            updateSetting(suggestionStoreKey, it)
        }.launchIn(scope)
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