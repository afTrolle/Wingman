package dev.trolle.wingman.db

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.runBlocking
import kotlinx.serialization.json.Json
import kotlinx.serialization.serializer

suspend inline fun <reified T> Database.set(key: String, value: T) =
    set(key, Json.serializersModule.serializer(), value)

suspend inline fun <reified T> Database.get(key: String): T? =
    get(key, Json.serializersModule.serializer())

inline fun <reified T> Database.getBlocking(key: String): T? =
    runBlocking { get(key, Json.serializersModule.serializer()) }

suspend inline fun <reified T> Database.getOrDefault(key: String, default: () -> T): T? =
    get(key, Json.serializersModule.serializer()) ?: default()

suspend inline fun <reified T> Database.getFlow(key: String): Flow<T?> =
    getFlow(key, Json.serializersModule.serializer())