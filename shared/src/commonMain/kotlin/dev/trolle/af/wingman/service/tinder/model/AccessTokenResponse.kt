package dev.trolle.af.wingman.service.tinder.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable


@Serializable
data class AccessTokenResponse(
    @SerialName("data")
    val `data`: Data?,
    @SerialName("meta")
    val meta: Meta?
) {
    @Serializable
    data class Data(
        @SerialName("api_token")
        val apiToken: String,
        @SerialName("_id")
        val id: String?,
        @SerialName("is_new_user")
        val isNewUser: Boolean?,
        @SerialName("refresh_token")
        val refreshToken: String
    )
}

