@file:Suppress("PropertyName")

package dev.trolle.wingman.ui.string

import androidx.compose.runtime.Composable
import androidx.compose.runtime.ReadOnlyComposable
import cafe.adriel.lyricist.LocalStrings

data class StringsDefinition(
    val phone_number_label: String,
    val disclaimer: String,
    val phone_number_placeholder: String,
    val app_name: String,
    val button_sign_in: String,
    val logo_desc: String,
    val error_something_went_wrong: String,
    val settings_description: String,
    val home_tab : String,
    val bio_tab : String,
    val profile_tab : String,

    val phone_otp_label: String,
    val phone_otp_desc: String,
    val phone_otp_caption: (phoneNumber: String) -> String,

    val email_otp_label: String,
    val email_otp_desc: String,
    val email_otp_caption: (email: String) -> String,
)

val Strings
    @Composable
    @ReadOnlyComposable
    get() = LocalStrings.current

val LocalStrings = LocalStrings
