package dev.trolle.af.wingman.compose

import androidx.compose.foundation.Image
import androidx.compose.foundation.interaction.PressInteraction
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.Button
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Scaffold
import androidx.compose.material.Text
import androidx.compose.material.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import dev.trolle.af.wingman.compose.local.FilterableInteractionSource
import dev.trolle.af.wingman.compose.local.systemBarsPadding
import dev.trolle.af.wingman.resources.Strings
import dev.trolle.af.wingman.screen.SignInState
import org.jetbrains.compose.resources.ExperimentalResourceApi
import org.jetbrains.compose.resources.painterResource


@Composable
fun SignIn(
    state: SignInState,
    onPhoneNumberChange: (String) -> Unit = {},
    onSignIn: () -> Unit = {},
    onPressInteractionConsumed: () -> Unit = {}
) = Scaffold { paddingValues ->
    Column(
        modifier = Modifier.fillMaxHeight()
            .padding(paddingValues)
            .systemBarsPadding()
            .padding(horizontal = 32.dp),
        verticalArrangement = Arrangement.SpaceBetween,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        var visible by remember { mutableStateOf(false) }
        LaunchedEffect(Unit) { visible = true }
        DefaultAnimatedVisibility(
            visible = visible,
            modifier = Modifier.weight(1f)
        ) {
            LogoWithText()
        }

        BottomInput(state, onPhoneNumberChange, onPressInteractionConsumed, onSignIn)
    }
}


@OptIn(ExperimentalResourceApi::class)
@Composable
private fun LogoWithText() =
    Column(
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Image(
            painter = painterResource("logo.png"),
            contentDescription = Strings.logo_desc,
            modifier = Modifier.size(250.dp)
        )
        Text(
            text = Strings.app_name,
            style = MaterialTheme.typography.h3
        )
    }


@Composable
private fun BottomInput(
    state: SignInState,
    onPhoneNumberChange: (String) -> Unit,
    onPressInteractionConsumed: () -> Unit,
    onSignIn: () -> Unit
) = Column(
    modifier = Modifier.padding(bottom = 40.dp),
    verticalArrangement = Arrangement.spacedBy(8.dp),
) {
    TextField(
        value = state.phoneNumber,
        isError = state.isValid?.not() ?: false,
        placeholder = { Text(Strings.phone_number_placeholder) },
        onValueChange = onPhoneNumberChange,
        modifier = Modifier.fillMaxWidth(),
        singleLine = true,
        keyboardOptions = KeyboardOptions(
            autoCorrect = false,
            keyboardType = KeyboardType.Phone,
        ),
        interactionSource = rememberInteractionsCaptureClickFilter(
            state,
            onPressInteractionConsumed
        )
    )

    val onButtonClick = remember(state.filterInteraction) {
        if (state.filterInteraction) onPressInteractionConsumed else onSignIn
    }
    Button(
        onClick = onButtonClick,
        modifier = Modifier.fillMaxWidth(),
    ) {
        Text(Strings.button_sign_in)
    }
}

@Composable
private fun rememberInteractionsCaptureClickFilter(
    state: SignInState,
    onPressInteractionFiltered: () -> Unit
) = remember(state.filterInteraction) {
    FilterableInteractionSource { interaction ->
        if (interaction is PressInteraction.Press && state.filterInteraction) {
            onPressInteractionFiltered()
        }
        interaction.takeUnless { state.filterInteraction }
    }
}