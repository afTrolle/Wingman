package dev.trolle.wingman.signin.preview

import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.tooling.preview.Preview
import dev.trolle.wingman.signin.OneTimePasswordState
import dev.trolle.wingman.signin.SignIn
import dev.trolle.wingman.signin.compose.OneTimePassword
import dev.trolle.wingman.ui.compose.PreviewDefaults

@Preview
@Composable
private fun SignInPreview() = PreviewDefaults {
    SignIn()
}

@Preview
@Composable
fun OneTimePasswordPreview() = PreviewDefaults {
    val (oneTimePassword, setOneTimePassword) = remember {
        mutableStateOf("")
    }
    val state = OneTimePasswordState(
        phoneNumber = "+46720208",
        oneTimePassword = oneTimePassword,
    )
    OneTimePassword(
        oneTimePasswordState = state,
        onOneTimePasswordChanged = setOneTimePassword,
    )
}