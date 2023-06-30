@file:OptIn(ExperimentalSerializationApi::class)

package tinder.services.authgateway

import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.Serializable
import kotlinx.serialization.protobuf.ProtoNumber

@Serializable
data class ErrorProto(
    @ProtoNumber(1)
    val code: Int,
    @ProtoNumber(2)
    val message: String,
    @ProtoNumber(3)
    val ban_reason: BanReason? = null,
)