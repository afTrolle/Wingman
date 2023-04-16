package dev.trolle.af.wingman.service

import com.russhwolf.settings.coroutines.FlowSettings
import dev.trolle.af.wingman.repository.SessionData
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json

interface PersistenceService {
    suspend fun setSessionData(sessionData: SessionData)

    suspend fun getSessionData(): Flow<SessionData?>
}


const val sessionDataKey = "sessionData"

// Consider using realm or sqlDelight,
fun persistenceService(
    settings: FlowSettings,
    json: Json
) = object : PersistenceService {

    override suspend fun setSessionData(sessionData: SessionData) {
        updateSetting(sessionDataKey, sessionData)
    }

    override suspend fun getSessionData() = settingFlow<SessionData>(sessionDataKey)

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

}