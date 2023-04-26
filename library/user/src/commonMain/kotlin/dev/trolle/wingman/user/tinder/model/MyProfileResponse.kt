package dev.trolle.wingman.user.tinder.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class MyProfileResponse(
    val badges: List<Badge> = emptyList(),
    @SerialName("age_filter_max")
    val ageFilterMax: Int? = null,
    @SerialName("age_filter_min")
    val ageFilterMin: Int? = null,
    val bio: String? = null,
    @SerialName("birth_date")
    val birthDate: String? = null,
    @SerialName("can_create_squad")
    val canCreateSquad: Boolean? = null,
    @SerialName("create_date")
    val createDate: String? = null,
    @SerialName("distance_filter")
    val distanceFilter: Int? = null,
    val gender: Int? = null,
    @SerialName("gender_filter")
    val genderFilter: Int? = null,
    @SerialName("_id")
    val id: String,
    @SerialName("interested_in")
    val interestedIn: List<Int> = emptyList(),
    val jobs: List<Job> = emptyList(),
    val name: String? = null,
    @SerialName("photo_optimizer_enabled")
    val photoOptimizerEnabled: Boolean? = null,
    val photos: List<Photo> = emptyList(),
    @SerialName("ping_time")
    val pingTime: String? = null,
    val schools: List<School> = emptyList(),
    @SerialName("show_gender_on_profile")
    val showGenderOnProfile: Boolean? = null,
)
