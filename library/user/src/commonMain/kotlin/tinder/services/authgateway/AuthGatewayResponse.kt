@file:OptIn(ExperimentalSerializationApi::class)

package tinder.services.authgateway

import com.google.protobuf.BoolValue
import com.google.protobuf.Int32Value
import com.google.protobuf.Int64Value
import com.google.protobuf.StringValue
import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.Serializable
import kotlinx.serialization.protobuf.ProtoNumber


@Serializable
data class ValidateEmailOtpState(
    @ProtoNumber(1)
    val refresh_token: StringValue? = null,
    @ProtoNumber(4)
    val otp_length: Int32Value? = null,
    @ProtoNumber(5)
    val email_sent: BoolValue,
    @ProtoNumber(6)
    val email_marketing: EmailMarketing? = null,

    // oneOf
    @ProtoNumber(2)
    val unmasked_email: String? = null,
    @ProtoNumber(3)
    val masked_email: String? = null,
)

@Serializable
data class EmailMarketing(
    @ProtoNumber(2)
    val show_marketing_opt_in: BoolValue,
    @ProtoNumber(3)
    val show_strict_opt_in: BoolValue,
    @ProtoNumber(4)
    val check_by_default: BoolValue,
)

@Serializable
enum class Captcha {
    @ProtoNumber(0)
    CAPTCHA_INVALID,

    @ProtoNumber(1)
    CAPTCHA_V1,

    @ProtoNumber(2)
    CAPTCHA_V2,
}

@Serializable
data class LoginResult(
    @ProtoNumber(1)
    val refresh_token: String,
    @ProtoNumber(2)
    val auth_Token: String,
    @ProtoNumber(3)
    val captcha: Captcha? = null,
    @ProtoNumber(4)
    val user_id: String,
    @ProtoNumber(5)
    val auth_token_ttl: Int64Value? = null,
)

@Serializable
data class ValidatePhoneOtpState(
    @ProtoNumber(1)
    val refresh_token: StringValue? = null,
    @ProtoNumber(2)
    val phone: String,
    @ProtoNumber(3)
    val otp_length: Int32Value? = null,
    @ProtoNumber(4)
    val sms_sent: BoolValue? = null,
)

@Serializable
data class AuthGatewayResponse(
    @ProtoNumber(1)
    val meta: MetaProto? = null,
    @ProtoNumber(2)
    val error: ErrorProto? = null,

    // oneOf
    @ProtoNumber(4)
    val validate_phone_otp_state: ValidatePhoneOtpState? = null,
    @ProtoNumber(6)
    val validate_email_otp_state: ValidateEmailOtpState? = null,
    @ProtoNumber(8)
    val login_result: LoginResult? = null,
)
