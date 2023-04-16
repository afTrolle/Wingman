package dev.trolle.af.wingman.screen

import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import cafe.adriel.voyager.core.screen.Screen
import dev.trolle.af.wingman.compose.OneTimePassword
import dev.trolle.af.wingman.ext.runCatchingCancelable
import dev.trolle.af.wingman.koin.getScreenModel
import dev.trolle.af.wingman.repository.UserRepository
import dev.trolle.af.wingman.screen.util.StateScreenModel
import dev.trolle.af.wingman.screen.util.launch
import dev.trolle.af.wingman.service.NavigationService
import io.github.aakira.napier.Napier
import org.koin.core.parameter.parametersOf

data class OneTimePasswordState(
    val phoneNumber: String,
    val oneTimePasswordLength: Int = 6,
    val oneTimePassword: String = "",
    val isOneTimePasswordError: Boolean = false,
    val signInEnabled: Boolean = true,
)

val OneTimePasswordState.isCorrectLength get() = oneTimePassword.length == oneTimePasswordLength

class OneTimePasswordModel(
    phoneNumber: String,
    private val userRepository: UserRepository,
    private val navigationService: NavigationService,
) : StateScreenModel<OneTimePasswordState>(
    OneTimePasswordState(phoneNumber)
) {
    fun onOneTimePasswordChanged(text: String) = launch {
        updateState {
            val oneTimePassword = text.filter { char -> char.isDigit() }
                .take(it.oneTimePasswordLength)

            val isOneTimePasswordError =
                if (oneTimePassword.isEmpty() && it.isOneTimePasswordError) false else it.isOneTimePasswordError

            it.copy(
                oneTimePassword = oneTimePassword,
                isOneTimePasswordError = isOneTimePasswordError
            )
        }
    }

    fun onSignIn() = launch {
        val state = updateStateAndGet {
            it.copy(isOneTimePasswordError = it.isCorrectLength)
        }
        if (state.isCorrectLength) {
            updateState { it.copy(signInEnabled = false) }
            runCatchingCancelable {
                userRepository.signInOneTimePassword(
                    oneTimePassword = state.oneTimePassword,
                    phoneNumber = state.phoneNumber
                )
            }.onFailure { error ->
                Napier.e("otp", error)
                updateState { it.copy(isOneTimePasswordError = true) }
                // TODO show error message
            }.onSuccess {
                navigationService.replaceAll(HomeScreen)
            }
            updateState { it.copy(signInEnabled = true) }
        }
    }
}

data class OneTimePasswordScreen(
    val phoneNumber: String
) : Screen {
    @Composable
    override fun Content() {
        // Can only be one instance of model in the stack
        val viewModel = getScreenModel<OneTimePasswordModel>() {
            parametersOf(phoneNumber)
        }
        val state by viewModel.state.collectAsState()
        OneTimePassword(
            state,
            viewModel::onOneTimePasswordChanged,
            viewModel::onSignIn
        )
    }
}
