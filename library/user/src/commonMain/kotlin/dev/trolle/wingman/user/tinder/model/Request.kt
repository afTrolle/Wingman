package dev.trolle.wingman.user.tinder.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
internal data class ApiTokenRequest(
    @SerialName("refresh_token")
    val refreshToken: String,
)

@Serializable
internal data class OtpRequest(
    @SerialName("phone_number")
    val phoneNumber: String,
)

@Serializable
internal data class RefreshApiTokenRequest(
    @SerialName("otp_code")
    val otp: String,
    @SerialName("phone_number")
    val phoneNumber: String,
)
