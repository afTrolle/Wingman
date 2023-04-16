package dev.trolle.af.wingman.android.preview

import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.tooling.preview.Preview
import dev.trolle.af.wingman.compose.OneTimePassword
import dev.trolle.af.wingman.compose.SignIn
import dev.trolle.af.wingman.screen.OneTimePasswordState
import dev.trolle.af.wingman.screen.SignInState

@Preview
@Composable
fun SignInPreview() = PreviewDefaults {
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
        onOneTimePasswordChanged = setOneTimePassword
    )
}