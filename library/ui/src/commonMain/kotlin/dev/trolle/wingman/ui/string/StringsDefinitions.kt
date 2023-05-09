package dev.trolle.wingman.ui.string

import androidx.compose.runtime.Composable
import androidx.compose.runtime.ReadOnlyComposable
import cafe.adriel.lyricist.LocalStrings

data class StringsDefinition(
    val phoneNumberSentTo: (phoneNumber: String) -> String,
    val one_time_password_help: String,
    val enterOneTimePassword: String,
    val phone_number_label: String,
    val disclaimer: String,
    val phone_number_placeholder: String,
    val app_name: String,
    val button_sign_in: String,
    val logo_desc: String,
    val error_something_went_wrong: String,
    val settings_description: String
)

val Strings
    @Composable
    @ReadOnlyComposable
    get() = LocalStrings.current

val LocalStrings = LocalStrings
