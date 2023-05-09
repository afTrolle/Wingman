package dev.trolle.wingman.user.tinder.model

import kotlinx.datetime.Instant
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import kotlinx.serialization.json.JsonObject

@Serializable
data class MatchesResponse(
    val `data`: Data? = null,
    val meta: Meta? = null,
) {
    @Serializable
    data class Data(
        val matches: List<Match> = emptyList(),
        @SerialName("next_page_token")
        val nextPageToken: String? = null,
    )
}

@Serializable
data class Match(
    val closed: Boolean? = null,
    @SerialName("common_friend_count")
    val commonFriendCount: Int? = null,
    @SerialName("common_like_count")
    val commonLikeCount: Int? = null,
    @SerialName("created_date")
    val createdDate: String? = null,
    val dead: Boolean? = null,
    val following: Boolean? = null,
    @SerialName("following_moments")
    val followingMoments: Boolean? = null,
    @SerialName("has_shown_initial_interest")
    val hasShownInitialInterest: Boolean? = null,
    @SerialName("_id")
    val idCase: String? = null,
    val id: String? = null,
    @SerialName("is_archived")
    val isArchived: Boolean? = null,
    @SerialName("is_boost_match")
    val isBoostMatch: Boolean? = null,
    @SerialName("is_experiences_match")
    val isExperiencesMatch: Boolean? = null,
    @SerialName("is_fast_match")
    val isFastMatch: Boolean? = null,
    @SerialName("is_matchmaker_match")
    val isMatchmakerMatch: Boolean? = null,
    @SerialName("is_opener")
    val isOpener: Boolean? = null,
    @SerialName("is_preferences_match")
    val isPreferencesMatch: Boolean? = null,
    @SerialName("is_primetime_boost_match")
    val isPrimetimeBoostMatch: Boolean? = null,
    @SerialName("is_super_boost_match")
    val isSuperBoostMatch: Boolean? = null,
    @SerialName("is_super_like")
    val isSuperLike: Boolean? = null,
    @SerialName("last_activity_date")
    val lastActivityDate: String? = null,
    @SerialName("liked_content")
    val likedContent: LikedContent? = null,
    @SerialName("message_count")
    val messageCount: Int,
    val messages: List<Message> = emptyList(),
    val participants: List<String> = emptyList(),
    val pending: Boolean? = null,
    val person: Person,
    @SerialName("readreceipt")
    val readReceipt: ReadReceipt? = null,
    val seen: Seen? = null,
) {

    @Serializable
    data class Message(
        val message: String? = null,
        @SerialName("sent_date")
        val sentDate: Instant? = null,
        val to: String? = null,
        val from: String? = null,
    )

    @Serializable
    data class LikedContent(
        @SerialName("by_closer")
        val byCloser: ByCloser? = null,
        @SerialName("by_opener")
        val byOpener: ByCloser? = null,
    ) {
        @Serializable
        data class ByCloser(
            @SerialName("is_swipe_note")
            val isSwipeNote: Boolean? = null,
            val photo: Photo? = null,
            val type: String? = null,
            @SerialName("user_id")
            val userId: String? = null,
        )
    }

    @Serializable
    data class Person(
        val bio: String? = null,
        @SerialName("birth_date")
        val birthDate: Instant?,
        val gender: Int,
        @SerialName("hide_age")
        val hideAge: Boolean? = null,
        @SerialName("hide_distance")
        val hideDistance: Boolean? = null,
        @SerialName("_id")
        val id: String,
        val name: String? = null,
        val photos: List<Photo> = emptyList(),
    )

    @Serializable
    data class ReadReceipt(val enabled: Boolean? = null)

    @Serializable
    data class Seen(@SerialName("match_seen") val matchSeen: Boolean? = null)
}
