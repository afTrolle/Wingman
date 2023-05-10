package dev.trolle.wingman.ui.string

import cafe.adriel.lyricist.LyricistStrings

@LyricistStrings(languageTag = Locales.EN, default = true)
internal val EnStrings = StringsDefinition(
    phoneNumberSentTo = { phoneNumber -> "SMS: $phoneNumber" },
    one_time_password_help =
    "You'll receive an SMS shortly with a login code. Charges for SMS and data usage may apply",
    enterOneTimePassword = "Enter one time password",
    phone_number_label = "Tinder account phone number",
    disclaimer = "Disclaimer: Use at your own risk. Not affiliated with Tinder LLC",
    phone_number_placeholder = "+467045024416",
    app_name = "Wingman",
    button_sign_in = "Sign in",
    logo_desc = "app logo",
    error_something_went_wrong = "Something went wrong",
    settings_description = "user settings",
    home_tab = "Home",
    bio_tab = "Bio",
    profile_tab = "Profile",
)
