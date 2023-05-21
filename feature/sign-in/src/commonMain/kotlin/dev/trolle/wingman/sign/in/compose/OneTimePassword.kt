

package dev.trolle.wingman.sign.`in`.compose

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import dev.trolle.wingman.sign.`in`.OneTimePasswordState
import dev.trolle.wingman.ui.compose.BigButton
import dev.trolle.wingman.ui.ext.imePadding
import dev.trolle.wingman.ui.ext.systemBarsPadding
import dev.trolle.wingman.ui.modifier.focusOnStart
import dev.trolle.wingman.ui.string.Strings

@Composable
fun OneTimePassword(
    oneTimePasswordState: OneTimePasswordState,
    onOneTimePasswordChanged: (String) -> Unit = {},
    onSignIn: () -> Unit = {},
) {
    @OptIn(ExperimentalMaterial3Api::class)
    Scaffold { paddingValues ->
        Box(
            Modifier.systemBarsPadding()
                .padding(paddingValues)
                .fillMaxSize()
                .padding(horizontal = 32.dp),
        ) {
            Column(
                modifier = Modifier.fillMaxWidth().fillMaxHeight(0.60f),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Bottom,
            ) {
                Text(
                    text = Strings.enterOneTimePassword,
                    textAlign = TextAlign.Center,
                    style = MaterialTheme.typography.headlineSmall,
                )
                Spacer(Modifier.height(44.dp))
                Text(
                    modifier = Modifier.padding(horizontal = 16.dp),
                    text = Strings.one_time_password_help,
                    textAlign = TextAlign.Center,
                    style = MaterialTheme.typography.headlineLarge,
                )
                Spacer(Modifier.height(32.dp))
                OneTimePasswordInput(
                    modifier = Modifier.focusOnStart(),
                    length = oneTimePasswordState.oneTimePasswordLength,
                    error = oneTimePasswordState.isOneTimePasswordError,
                    text = oneTimePasswordState.oneTimePassword,
                    onTextChanged = { onOneTimePasswordChanged(it.text) },
                )
                Text(
                    text = Strings.phoneNumberSentTo(oneTimePasswordState.phoneNumber),
                    modifier = Modifier.padding(top = 12.dp, bottom = 16.dp),
                    style = MaterialTheme.typography.bodySmall,
                    textAlign = TextAlign.Center,
                )
            }

            BigButton(
                Strings.button_sign_in,
                onSignIn,
                modifier = Modifier
                    .align(Alignment.BottomCenter)
                    .imePadding()
                    .padding(bottom = 8.dp),
            )
        }
    }
}
