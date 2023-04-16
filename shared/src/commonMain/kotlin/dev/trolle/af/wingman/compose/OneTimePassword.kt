package dev.trolle.af.wingman.compose

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Button
import androidx.compose.material.Scaffold
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.unit.dp
import dev.trolle.af.wingman.compose.local.systemBarsPadding
import dev.trolle.af.wingman.resources.Strings
import dev.trolle.af.wingman.screen.OneTimePasswordState

@Composable
fun OneTimePassword(
    oneTimePasswordState: OneTimePasswordState,
    onOneTimePasswordChanged: (String) -> Unit = {},
    onSignIn: () -> Unit = {}
) {
    Scaffold { paddingValues ->
        Box(
            modifier = Modifier.systemBarsPadding()
                .padding(paddingValues)
                .fillMaxSize()
        ) {
            OneTimePasswordInput(
                modifier = Modifier.fillMaxHeight(0.5f).focusOnStart()
                    .align(Alignment.BottomCenter),
                length = oneTimePasswordState.length,
                text = oneTimePasswordState.oneTimePassword,
                onTextChanged = { onOneTimePasswordChanged(it.text) }
            )

            Button(
                onClick = onSignIn,
                contentPadding = PaddingValues(horizontal = 4.dp),
                modifier = Modifier.fillMaxWidth().padding(horizontal = 32.dp, vertical = 16.dp)
                    .align(Alignment.BottomCenter)
            ) {
                Text(Strings.button_sign_in)
            }
        }
    }
}


@Composable
fun Modifier.focusOnStart() = focusRequester(focusRequester = StartFocusRequester())

@Composable
fun StartFocusRequester(): FocusRequester {
    val focusRequester = FocusRequester()
    LaunchedEffect(Unit) {
        focusRequester.requestFocus()
    }
    return focusRequester
}
