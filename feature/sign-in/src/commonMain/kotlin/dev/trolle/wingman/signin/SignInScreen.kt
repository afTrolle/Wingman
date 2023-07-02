package dev.trolle.wingman.signin

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import cafe.adriel.voyager.core.model.coroutineScope
import cafe.adriel.voyager.core.screen.Screen
import dev.trolle.wingman.common.ext.runCatchingCancelable
import dev.trolle.wingman.signin.compose.SignInLayout
import dev.trolle.wingman.signin.service.PhoneNumberService
import dev.trolle.wingman.signin.service.PhoneValidateService
import dev.trolle.wingman.ui.Navigation
import dev.trolle.wingman.ui.StringsContainer
import dev.trolle.wingman.ui.ext.getScreenModel
import dev.trolle.wingman.ui.ext.launch
import dev.trolle.wingman.ui.voyager.StateScreenModel
import dev.trolle.wingman.user.User
import io.github.aakira.napier.Napier
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.plus

private fun SignInState.updatePhoneNumber(
    number: String,
    validation: PhoneValidateService,
): SignInState {
    val filteredNumber = number.filter { it.isDigit() || it == '+' }
    val isError = when {
        filteredNumber.isEmpty() -> null // reset validation
        isError == true -> !validation.isPhoneNumberValid(filteredNumber)
        else -> isError
    }
    return copy(
        phoneNumber = filteredNumber,
        isError = isError,
    )
}

internal class SignInScreenModel(
    private val phoneValidateService: PhoneValidateService,
    private val userRepository: User,
    private val navigationService: Navigation,
    private val phoneNumberService: PhoneNumberService,
    private val stringsContainer: StringsContainer,
) : StateScreenModel<SignInState>(
    initialState = SignInState(),
) {
    init {
        phoneNumberService.phoneNumber.onEach { result ->
            result.onSuccess {
                val initialPhoneNumber = result.getOrNull() ?: ""
                updateState {
                    it.updatePhoneNumber(initialPhoneNumber, phoneValidateService)
                        .copy(requestFocus = false, captureFocus = false)
                }
            }.onFailure {
                updateState {
                    it.updatePhoneNumber("", phoneValidateService)
                        .copy(requestFocus = true, captureFocus = false)
                }
            }
        }.launchIn(coroutineScope + Dispatchers.Default)
    }

    fun onPhoneNumberChange(number: String) = updateStateUnSafe {
        it.updatePhoneNumber(number, phoneValidateService)
    }

    fun onSignIn() = launch {
        val state = updateStateAndGet {
            it.copy(isError = !phoneValidateService.isPhoneNumberValid(it.phoneNumber))
        }
        if (state.isError == false) {
            updateState { it.copy(isButtonEnabled = false) }
            runCatchingCancelable {
                userRepository.signInRequestOneTimePassword(state.phoneNumber)
            }.onFailure { error ->
                Napier.e("Sign In Error", error)
                updateState { it.copy(errorMessage = stringsContainer.strings.error_something_went_wrong) }
            }.onSuccess {
                navigationService.open(OneTimePasswordScreen(OneTimePasswordConfig.Phone(state.phoneNumber)))
            }
            updateState { it.copy(isButtonEnabled = true) }
        }
    }

    fun onPressConsumed() = launch {
        phoneNumberService.getPhoneNumber()
    }
}

object SignInScreen : Screen {
    @Composable
    override fun Content() {
        val viewModel = getScreenModel<SignInScreenModel>()
        val state by viewModel.state
        SignInLayout(
            text = state.phoneNumber,
            isError = state.isError,
            isSignInEnabled = state.isButtonEnabled,
            captureFocus = state.captureFocus,
            requestFocus = state.requestFocus,
            onChange = viewModel::onPhoneNumberChange,
            onSignIn = viewModel::onSignIn,
            onFocusCaptured = viewModel::onPressConsumed,
        )
    }
}
