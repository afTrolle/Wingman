package dev.trolle.wingman.sign.`in`.compose

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.onFocusEvent
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import dev.trolle.wingman.sign.`in`.SignInState
import dev.trolle.wingman.ui.compose.BigButton
import dev.trolle.wingman.ui.compose.DefaultAnimatedVisibility
import dev.trolle.wingman.ui.ext.systemBarsPadding
import dev.trolle.wingman.ui.string.Strings
import org.jetbrains.compose.resources.ExperimentalResourceApi
import org.jetbrains.compose.resources.painterResource


@Composable
fun SignIn(
    state: SignInState,
    onPhoneNumberChange: (String) -> Unit = {},
    onSignIn: () -> Unit = {},
    onPressInteractionConsumed: () -> Unit = {},
    ) =
    @OptIn(ExperimentalMaterial3Api::class)
    Scaffold { paddingValues ->
        Box(
            modifier = Modifier.fillMaxHeight()
                .padding(paddingValues)
                .systemBarsPadding()
                .padding(horizontal = 32.dp),
        ) {
            Column(
                modifier = Modifier.align(Alignment.Center),
                verticalArrangement = Arrangement.spacedBy(8.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
            ) {
                DefaultAnimatedVisibility(
                    visible = state.isLogoVisible,
                    modifier = Modifier.height(350.dp),
                ) {
                    LogoWithText()
                }
                BottomInput(state, onPhoneNumberChange, onPressInteractionConsumed, onSignIn)
            }

            Text(
                text = Strings.disclaimer,
                modifier = Modifier.padding(horizontal = 16.dp, vertical = 16.dp)
                    .align(Alignment.BottomCenter),
                textAlign = TextAlign.Center,
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.tertiary,
            )
        }
    }

@OptIn(ExperimentalResourceApi::class)
@Composable
private fun LogoWithText() =
    Column(
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        Image(
            painter = painterResource("logo.png"),
            contentDescription = Strings.logo_desc,
            modifier = Modifier.size(250.dp),
        )
        Text(
            text = Strings.app_name,
            style = MaterialTheme.typography.displayMedium,
        )
    }


@Composable
private fun BottomInput(
    state: SignInState,
    onPhoneNumberChange: (String) -> Unit,
    onPressInteractionConsumed: () -> Unit,
    onSignIn: () -> Unit,
) = Column(
    modifier = Modifier.padding(bottom = 40.dp),
    verticalArrangement = Arrangement.spacedBy(8.dp),
) {
    val focusManager = LocalFocusManager.current
    @OptIn(ExperimentalMaterial3Api::class)
    TextField(
        value = state.phoneNumber,
        isError = state.isValid?.not() ?: false,
        label = { Text(Strings.phone_number_label) },
        placeholder = { Text(Strings.phone_number_placeholder) },
        onValueChange = onPhoneNumberChange,
        modifier = Modifier.fillMaxWidth()
            .onFocusEvent {
                if (it.isFocused && state.filterInteraction) {
                    onPressInteractionConsumed()
                    focusManager.clearFocus()
                }
            },
        singleLine = true,
        keyboardOptions = KeyboardOptions(
            autoCorrect = false,
            keyboardType = KeyboardType.Phone,
        ),
    )

    val onButtonClick = remember(state.filterInteraction) {
        if (state.filterInteraction) onPressInteractionConsumed else onSignIn
    }
    BigButton(
        onClick = onButtonClick,
        modifier = Modifier.fillMaxWidth(),
        enabled = state.isButtonEnabled,
        text = Strings.button_sign_in,
    )
}
