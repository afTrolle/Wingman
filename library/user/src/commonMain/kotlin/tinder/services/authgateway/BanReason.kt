@file:OptIn(ExperimentalSerializationApi::class)

package tinder.services.authgateway

import com.google.protobuf.BoolValue
import com.google.protobuf.Int64Value
import com.google.protobuf.StringValue
import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.Serializable
import kotlinx.serialization.protobuf.ProtoNumber

@Serializable
data class BanReason(
    @ProtoNumber(1)
    val underage_ban: UnderageBan? = null,
    @ProtoNumber(2)
    val ban_appeal: BanAppeal? = null,
)

@Serializable
data class BanAppeal(
    @ProtoNumber(1)
    val challenge_type: String,
    @ProtoNumber(2)
    val challenge_token: String,
    @ProtoNumber(3)
    val refresh_token: String,
)

@Serializable
data class UnderageBan(
    @ProtoNumber(1)
    val underage_ttl_duration_ms: Int64Value? = null,
    @ProtoNumber(2)
    val underage_token: StringValue? = null,
    @ProtoNumber(3)
    val verification: Verification? = null,
    @ProtoNumber(3)
    val is_onboarding: BoolValue? = null,
)

@Serializable
data class Verification(
    @ProtoNumber(1)
    val type: String,
    @ProtoNumber(2)
    val state: String,
)