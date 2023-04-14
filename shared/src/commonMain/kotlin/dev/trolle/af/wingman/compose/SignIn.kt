package dev.trolle.af.wingman.compose

import androidx.compose.foundation.Image
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
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import dev.trolle.af.wingman.resources.Strings
import dev.trolle.af.wingman.screen.SignInState
import org.jetbrains.compose.resources.ExperimentalResourceApi
import org.jetbrains.compose.resources.painterResource


@Composable
fun SignIn(
    state: SignInState,
    onPhoneNumberChange: (String) -> Unit = {},
    onSignIn: () -> Unit = {},
    onPhoneNumberFocus: () -> Unit = {}
) {
    Scaffold { paddingValues ->
        Column(
            Modifier.fillMaxHeight().padding(paddingValues).padding(horizontal = 32.dp),
            verticalArrangement = Arrangement.SpaceEvenly,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            @OptIn(ExperimentalResourceApi::class) val logo = painterResource("logo.png")
            Column(
                modifier = Modifier.weight(1.0f),
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Image(
                    logo, Strings.logo_desc, modifier = Modifier.size(250.dp)
                )
                Text(
                    Strings.app_name, style = MaterialTheme.typography.h3
                )
            }

            Column(
                modifier = Modifier.padding(bottom = 40.dp),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                TextField(
                    value = state.phoneNumber,
                    placeholder = { Text(Strings.phone_number_placeholder) },
                    onValueChange = onPhoneNumberChange,
                    modifier = Modifier.fillMaxWidth().onFocusChanged {
                        if (it.isFocused) {
                            onPhoneNumberFocus()
                        }
                    },
                    keyboardOptions = KeyboardOptions(
                        autoCorrect = false,
                        keyboardType = KeyboardType.Phone,
                    ),
                )
                Button(
                    onSignIn, Modifier.fillMaxWidth()
                ) {
                    Text(Strings.button_sign_in)
                }
            }
        }
    }
}