package dev.trolle.af.wingman.service.tinder.model

import kotlinx.datetime.Instant
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class ProfileResponse(
    val results: Profile? = null,
    val status: Int? = null,
) {

    @Serializable
    data class Profile(
        val badges: List<Badge>,
        val bio: String? = null,
        @SerialName("birth_date")
        val birthDate: Instant? = null,
        @SerialName("birth_date_info")
        val birthDateInfo: String,
        val city: City? = null,
        @SerialName("connection_count")
        val connectionCount: Int,
        @SerialName("distance_mi")
        val distanceMi: Int,
        val gender: Int,
        @SerialName("_id")
        val id: String,
        @SerialName("is_tinder_u")
        val isTinderU: Boolean,
        val jobs: List<Job> = emptyList(),
        val name: String,
        val photos: List<Photo> = emptyList(),
        @SerialName("ping_time")
        val pingTime: String,
        @SerialName("s_number")
        val sNumber: Long,
        val schools: List<School> = emptyList(),
        @SerialName("selected_descriptors")
        val selectedDescriptors: List<SelectedDescriptor> = emptyList(),
        @SerialName("sexual_orientations")
        val sexualOrientations: List<SexualOrientation>? = null,
        @SerialName("show_gender_on_profile")
        val showGenderOnProfile: Boolean? = null,
        @SerialName("show_orientation_on_profile")
        val showOrientationOnProfile: Boolean? = null,
        @SerialName("spotify_theme_track")
        val spotifyThemeTrack: SpotifyThemeTrack? = null,
        val teasers: List<Teaser> = emptyList(),
        @SerialName("user_interests")
        val userInterests: UserInterests = UserInterests(),
    ) {

        @Serializable
        data class City(val name: String? = null)

        @Serializable
        data class SelectedDescriptor(
            @SerialName("choice_selections")
            val choiceSelections: List<ChoiceSelection> = emptyList(),
            @SerialName("icon_url")
            val iconUrl: String? = null,
            @SerialName("icon_urls")
            val iconUrls: List<IconUrl> = emptyList(),
            val id: String,
            val name: String? = null,
            val prompt: String? = null,
            @SerialName("section_id")
            val sectionId: String,
            @SerialName("section_name")
            val sectionName: String,
            val type: String,
        ) {
            @Serializable
            data class ChoiceSelection(
                val emoji: String? = null,
                @SerialName("icon_urls")
                val iconUrls: List<IconUrl> = emptyList(),
                val id: String,
                val name: String,
                val style: String? = null,
            )

            @Serializable
            data class IconUrl(
                val height: Int,
                val quality: String,
                val url: String,
                val width: Int,
            )
        }

        @Serializable
        data class SexualOrientation(val id: String, val name: String)

        @Serializable
        data class SpotifyThemeTrack(
            val album: Album,
            val artists: List<Artist> = emptyList(),
            val id: String,
            val name: String,
            @SerialName("preview_url")
            val previewUrl: String,
            val uri: String,
        ) {
            @Serializable
            data class Album(
                val id: String,
                val images: List<Image> = emptyList(),
                val name: String,
            ) {
                @Serializable
                data class Image(
                    val height: Int,
                    val url: String,
                    val width: Int,
                )
            }

            @Serializable
            data class Artist(val id: String, val name: String)
        }

        @Serializable
        data class Teaser(val string: String, val type: String)

        @Serializable
        data class UserInterests(
            @SerialName("selected_interests")
            val selectedInterests: List<SelectedInterest> = emptyList(),
        ) {
            @Serializable
            data class SelectedInterest(val id: String, val name: String)
        }
    }
}
