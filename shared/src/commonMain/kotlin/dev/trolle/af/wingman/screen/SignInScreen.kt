package dev.trolle.af.wingman.screen

import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import cafe.adriel.voyager.core.lifecycle.LifecycleEffect
import cafe.adriel.voyager.core.model.coroutineScope
import cafe.adriel.voyager.core.screen.Screen
import dev.trolle.af.wingman.compose.SignIn
import dev.trolle.wingman.common.ext.runCatchingCancelable
import dev.trolle.wingman.ui.voyager.StateScreenModel
import dev.trolle.af.wingman.service.PhoneNumberService
import dev.trolle.af.wingman.service.PhoneValidateService
import dev.trolle.af.wingman.service.numberUpdates
import dev.trolle.af.wingman.service.shouldFetchPhoneNumber
import dev.trolle.wingman.ui.Navigation
import dev.trolle.wingman.ui.ext.getScreenModel
import dev.trolle.wingman.ui.ext.launch
import dev.trolle.wingman.ui.string.StringsDefinition
import dev.trolle.wingman.user.User
import io.github.aakira.napier.Napier
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach

data class SignInState(
    val phoneNumber: String = "",
    val isValid: Boolean? = null,
    val filterInteraction: Boolean = true,
    val isButtonEnabled: Boolean = true,
    val errorMessage: String? = null,
    val isLogoVisible: Boolean = false,
)

private fun SignInState.updatePhoneNumber(
    number: String,
    validation: PhoneValidateService,
): SignInState {
    val filteredNumber = number.filter { it.isDigit() || it == '+' }
    val updatedIsValid = when {
        filteredNumber.isEmpty() -> null // reset validation
        isValid == false -> validation.isPhoneNumberValid(filteredNumber)
        else -> isValid
    }
    return copy(
        phoneNumber = filteredNumber,
        isValid = updatedIsValid,
        filterInteraction = false,
    )
}

internal class SignInScreenModel(
    private val phoneValidateService: PhoneValidateService,
    private val userRepository: User,
    private val navigationService: Navigation,
    private val phoneNumberService: PhoneNumberService,
    private val strings : StateFlow<StringsDefinition>,
) : StateScreenModel<SignInState>(
    initialState = SignInState(
        filterInteraction = phoneNumberService.shouldFetchPhoneNumber,
    ),
) {
    init {
        phoneNumberService.numberUpdates.onEach { result ->
            val initialPhoneNumber = result?.getOrNull() ?: ""
            updateState {
                it.updatePhoneNumber(initialPhoneNumber, phoneValidateService)
            }
        }.launchIn(coroutineScope)
    }

    fun onPhoneNumberChange(number: String?) {
        updateState {
            it.updatePhoneNumber(number ?: "", phoneValidateService)
        }
    }

    fun onSignIn() = launch {
        val state = updateStateAndGet {
            it.copy(isValid = phoneValidateService.isPhoneNumberValid(it.phoneNumber))
        }
        if (state.isValid == true) {
            updateState { it.copy(isButtonEnabled = false) }
            runCatchingCancelable {
                userRepository.signInRequestOneTimePassword(state.phoneNumber)
            }.onFailure { error ->
                Napier.e("Sign In Error", error)
                updateState { it.copy(errorMessage = strings.value.error_something_went_wrong) }
            }.onSuccess {
                navigationService.open(OneTimePasswordScreen(state.phoneNumber))
            }
            updateState { it.copy(isButtonEnabled = true) }
        }
    }

    fun onStart() {
        // Set once so that logo animation is only done once
        updateState { it.copy(isLogoVisible = true) }
    }

    fun onPressConsumed() = launch {
        phoneNumberService.getPhoneNumber()
    }
}

internal object SignInScreen : Screen {
    @Composable
    override fun Content() {
        val viewModel = getScreenModel<SignInScreenModel>()
        val state by viewModel.state.collectAsState()
        LifecycleEffect(onStarted = viewModel::onStart)

        SignIn(
            state,
            viewModel::onPhoneNumberChange,
            viewModel::onSignIn,
            viewModel::onPressConsumed,
        )
    }
}
