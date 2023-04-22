package dev.trolle.af.wingman.service.tinder.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import kotlinx.serialization.json.JsonObject

@Serializable
data class Meta(
    @SerialName("status")
    val status: Int? = null,
)

@Serializable
data class Badge(val type: String)

@Serializable
data class School(
    val displayed: Boolean? = null,
    val name: String? = null,
)

@Serializable
data class Job(
    val company: Company? = null,
    val title: Title? = null,
) {
    @Serializable
    data class Company(val name: String)

    @Serializable
    data class Title(val name: String)
}

@Serializable
data class Photo(
    val assets: List<JsonObject> = emptyList(),
    val extension: String? = null,
    val fileName: String? = null,
    val id: String? = null,
    @SerialName("media_type")
    val mediaType: String? = null,
    val processedFiles: List<ProcessedFile> = emptyList(),
    val rank: Int? = null,
    val score: Double? = null,
    val type: String? = null,
    val url: String? = null,
    @SerialName("webp_qf")
    val webpQf: List<Int?> = emptyList(),
    @SerialName("win_count")
    val winCount: Int? = null,
) {
    @Serializable
    data class ProcessedFile(
        val height: Int? = null,
        val url: String? = null,
        val width: Int? = null,
    )
}
