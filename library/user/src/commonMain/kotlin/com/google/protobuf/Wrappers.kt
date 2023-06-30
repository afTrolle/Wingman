@file:OptIn(ExperimentalSerializationApi::class)

package com.google.protobuf

import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.Serializable
import kotlinx.serialization.protobuf.ProtoNumber


@Serializable
data class DoubleValue(
    @ProtoNumber(1)
    val value: Double,
)

@Serializable
data class FloatValue(
    @ProtoNumber(1)
    val value: Float,
)

@Serializable
data class Int64Value(
    @ProtoNumber(1)
    val value: Long, // int64
)

@Serializable
data class UInt64Value(
    @ProtoNumber(1)
    val value: ULong, // uint64
)

@Serializable
data class Int32Value(
    @ProtoNumber(1)
    val value: Int,
)

@Serializable
data class UInt32Value(
    @ProtoNumber(1)
    val value: UInt,
)

@Serializable
data class BoolValue(
    @ProtoNumber(1)
    val value: Boolean = false,
)

@Serializable
data class StringValue(
    @ProtoNumber(1)
    val value: String,
)

@Serializable
data class BytesValue(
    @ProtoNumber(1)
    val value: String, // ByteString
)
