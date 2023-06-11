package dev.trolle.wingman.signin.compose

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.defaultMinSize
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import dev.trolle.wingman.signin.OneTimePasswordState
import dev.trolle.wingman.ui.MaterialThemeWingman
import dev.trolle.wingman.ui.compose.BigButton
import dev.trolle.wingman.ui.compose.Pane
import dev.trolle.wingman.ui.string.Strings

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun OneTimePasswordLayout(
    oneTimePasswordState: OneTimePasswordState,
    onOneTimePasswordChanged: (String) -> Unit = {},
    onSignIn: () -> Unit = {},
) = Scaffold(
    containerColor = MaterialThemeWingman.colorScheme.secondaryContainer,
    contentColor = MaterialThemeWingman.colorScheme.onSurface,
) { paddingValues ->
    Pane(
        Modifier.padding(paddingValues),
        innerModifier = Modifier.fillMaxSize(),
        containerColor = MaterialThemeWingman.colorScheme.surface,
    ) {
        val scroll = rememberScrollState()
        val hasFocus = remember { mutableStateOf(false) }
        Column(
            modifier = Modifier.fillMaxHeight().widthIn(max = 420.dp).padding(horizontal = 16.dp).verticalScroll(scroll),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center,
        ) {
            Spacer(Modifier.weight(1f))
            Text(
                modifier = Modifier.padding(top = 16.dp),
                text = Strings.enterOneTimePassword,
                textAlign = TextAlign.Center,
                style = MaterialTheme.typography.displaySmall,
            )
            Spacer(Modifier.height(32.dp))
            Text(
                modifier = Modifier.padding(horizontal = 16.dp),
                text = Strings.one_time_password_help,
                textAlign = TextAlign.Center,
                style = MaterialTheme.typography.bodyLarge,
            )
            Spacer(Modifier.height(32.dp))
            OneTimePasswordInput(
                length = oneTimePasswordState.oneTimePasswordLength,
                error = oneTimePasswordState.isOneTimePasswordError,
                text = oneTimePasswordState.oneTimePassword,
                onTextChanged = { onOneTimePasswordChanged(it) },
            )
            Text(
                text = Strings.phoneNumberSentTo(oneTimePasswordState.phoneNumber),
                modifier = Modifier.padding(top = 12.dp, bottom = 16.dp),
                style = MaterialTheme.typography.bodySmall,
                textAlign = TextAlign.Center,
            )
            Spacer(Modifier.weight(1f))
            BigButton(
                Strings.button_sign_in,
                onSignIn,
                modifier = Modifier.fillMaxWidth().padding(bottom = 16.dp),
            )

        }
    }
}
