package dev.trolle.af.wingman.android.preview

import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import dev.trolle.af.wingman.compose.OneTimePassword
import dev.trolle.af.wingman.compose.SignIn
import dev.trolle.af.wingman.screen.OneTimePasswordState
import dev.trolle.af.wingman.screen.SignInState

@Preview(
    showSystemUi = true,
    showBackground = true
)
@Composable
fun SignInPreview() = PreviewDefaults {
    SignIn(state = SignInState())
}

@Preview
@Composable
fun OneTimePasswordPreview() = PreviewDefaults {
    OneTimePassword(OneTimePasswordState("1234568"))
}