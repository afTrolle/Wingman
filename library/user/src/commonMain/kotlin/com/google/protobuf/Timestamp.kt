@file:OptIn(ExperimentalSerializationApi::class)

package com.google.protobuf

import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.Serializable
import kotlinx.serialization.protobuf.ProtoNumber

@Serializable
data class Timestamp(
    @ProtoNumber(1)
    val seconds: Long,
    @ProtoNumber(2)
    val nanos: Int,
)