package dev.trolle.wingman.signin

data class SignInState(
    val phoneNumber: String = "",
    val isError: Boolean? = null,
    val isButtonEnabled: Boolean = true,
    val errorMessage: String? = null,
    val isLogoVisible: Boolean = false,
    val captureFocus: Boolean = true,
    val requestFocus: Boolean = false,
)