package dev.trolle.wingman.sign.`in`.preview

import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.tooling.preview.Preview
import dev.trolle.wingman.sign.`in`.OneTimePasswordState
import dev.trolle.wingman.sign.`in`.SignInState
import dev.trolle.wingman.sign.`in`.compose.OneTimePassword
import dev.trolle.wingman.sign.`in`.compose.SignIn
import dev.trolle.wingman.ui.compose.PreviewDefaults

@Preview
@Composable
private fun SignInPreview() = PreviewDefaults {
    val (text, setText) = remember { mutableStateOf("") }
    SignIn(
        state = SignInState(
            phoneNumber = text,
            filterInteraction = false,
        ),
        onPhoneNumberChange = setText,
    )
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
