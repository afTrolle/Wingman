package dev.trolle.wingman.db

import com.russhwolf.settings.coroutines.FlowSettings
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.mapNotNull
import kotlinx.serialization.DeserializationStrategy
import kotlinx.serialization.KSerializer
import kotlinx.serialization.SerializationStrategy
import kotlinx.serialization.json.Json

interface Database {
    suspend fun <T> set(key: String, serializer: SerializationStrategy<T>, value: T)
    suspend fun <T> get(key: String, deserializer: DeserializationStrategy<T>): T?
    suspend fun <T> getOrSet(key: String, serializer: KSerializer<T>, default: () -> T): T
    suspend fun <T> getFlow(key: String, deserializer: DeserializationStrategy<T>): Flow<T>
}

internal fun db(
    settings: FlowSettings,
    json: Json,
) = object : Database {

    override suspend fun <T> set(key: String, serializer: SerializationStrategy<T>, value: T) {
        val jsonValue = json.encodeToString(serializer, value)
        settings.putString(key, jsonValue)
    }

    // Note doesn't handle object being null.
    override suspend fun <T> getOrSet(
        key: String,
        serializer: KSerializer<T>,
        default: () -> T,
    ): T {
        val stored = get(key, serializer)
        return if (stored != null) {
            stored
        } else {
            val toStore = default()
            set(key, serializer, toStore)
            toStore
        }
    }

    override suspend fun <T> get(key: String, deserializer: DeserializationStrategy<T>): T? =
        settings.getStringOrNull(key)?.decodeString(deserializer)

    override suspend fun <T> getFlow(
        key: String,
        deserializer: DeserializationStrategy<T>,
    ): Flow<T> =
        settings.getStringOrNullFlow(key).mapNotNull { it.decodeString(deserializer) }

    private fun <T> String?.decodeString(
        deserializer: DeserializationStrategy<T>,
    ) = this?.let { json.decodeFromString(deserializer, it) }
}
