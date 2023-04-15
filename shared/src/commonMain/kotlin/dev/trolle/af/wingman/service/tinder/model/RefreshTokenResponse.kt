package dev.trolle.af.wingman.service.tinder.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class RefreshTokenResponse(
    @SerialName("data")
    val `data`: Data,
    @SerialName("meta")
    val meta: Meta? = null
) {

    @Serializable
    data class Data(
        @SerialName("refresh_token")
        val refreshToken: String,
        @SerialName("validated")
        val validated: Boolean? = null
    )

}

