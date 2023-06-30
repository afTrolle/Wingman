@file:OptIn(ExperimentalSerializationApi::class)

package tinder.services.authgateway

import com.google.protobuf.StringValue
import com.google.protobuf.Timestamp
import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.Serializable
import kotlinx.serialization.protobuf.ProtoNumber

@Serializable
data class Phone(
    @ProtoNumber(1)
    val phone: String,
    @ProtoNumber(2)
    val refresh_token: StringValue? = null,
    // oneOf
    @ProtoNumber(3)
    val captcha_token: StringValue? = null,
    @ProtoNumber(4)
    val ios_device_token: StringValue? = null,
    @ProtoNumber(5)
    val android_jws: StringValue? = null,
)

@Serializable
data class AuthGatewayRequest(
    @ProtoNumber(1)
    val phone: Phone? = null,
    @ProtoNumber(2)
    val phone_otp: PhoneOtp? = null,
    @ProtoNumber(5)
    val email_otp: EmailOtp? = null,
)

@Serializable
data class EmailOtp(
    @ProtoNumber(1)
    val email : StringValue? = null,
    @ProtoNumber(2)
    val otp : String,
    @ProtoNumber(3)
    val refresh_token: StringValue? = null,
)

@Serializable
data class PhoneOtp(
    @ProtoNumber(1)
    val phone: StringValue? = null,
    @ProtoNumber(2)
    val otp: String,
    @ProtoNumber(3)
    val refresh_token: StringValue? = null,
)

@Serializable
data class MetaProto(
    @ProtoNumber(1)
    val upstream_time: Timestamp? = null,
)


