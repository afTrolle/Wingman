package dev.trolle.af.wingman.screen

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberCoroutineScope
import cafe.adriel.voyager.core.model.StateScreenModel
import cafe.adriel.voyager.core.screen.Screen
import dev.trolle.af.wingman.compose.SignIn
import dev.trolle.af.wingman.compose.local.LocalPhoneNumberProvider
import dev.trolle.af.wingman.koin.getScreenModel
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

data class SignInState(
    val phoneNumber: String = "", val requestPhoneNumber: Boolean = false
) {

}

object SignInScreen : Screen {
    class SignInScreenModel(
    ) : StateScreenModel<SignInState>(
        SignInState()
    ) {

        fun onPhoneNumberChange(number: String) {
            mutableState.update {
                it.copy(phoneNumber = number)
            }
        }

        fun onSignIn() {
            // TODO launch OTP Request

        }

        fun initialPhoneNumber(number: String?) {
            if (number == null)
                return
            mutableState.update { state ->
                if (state.phoneNumber.isBlank()) {
                    state.copy(phoneNumber = number)
                } else {
                    state
                }
            }
        }
    }

    @Composable
    override fun Content() {
        val viewModel = getScreenModel<SignInScreenModel>()
        val state by viewModel.state.collectAsState()

        val phoneNumberProvider = LocalPhoneNumberProvider.current
        val phoneNumberState = phoneNumberProvider?.phoneNumber?.collectAsState()
        val number = phoneNumberState?.value
        LaunchedEffect(number) {
            viewModel.initialPhoneNumber(number?.getOrNull())
        }
        val scope = rememberCoroutineScope()

        SignIn(
            state,
            viewModel::onPhoneNumberChange,
            viewModel::onSignIn,
            onPhoneNumberFocus = {
                phoneNumberProvider?.run {
                    if (number == null) scope.launch {
                        fetchPhoneNumber()
                    }
                }
            })
    }
}

