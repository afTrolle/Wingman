package dev.trolle.wingman.db

import com.russhwolf.settings.coroutines.FlowSettings
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.mapNotNull
import kotlinx.serialization.DeserializationStrategy
import kotlinx.serialization.SerializationStrategy
import kotlinx.serialization.json.Json

interface Database {
    suspend fun <T> set(key: String, serializer: SerializationStrategy<T>, value: T)
    suspend fun <T> get(key: String, deserializer: DeserializationStrategy<T>): T?
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

    override suspend fun <T> get(key: String, deserializer: DeserializationStrategy<T>): T? =
        settings.getStringOrNull(key)?.let { json.decodeFromString(deserializer, it) }

    override suspend fun <T> getFlow(
        key: String,
        deserializer: DeserializationStrategy<T>,
    ): Flow<T> =
        settings.getStringOrNullFlow(key).mapNotNull {
            it?.let { json.decodeFromString(deserializer, it) }
        }
}
