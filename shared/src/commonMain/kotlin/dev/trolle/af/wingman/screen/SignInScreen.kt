package dev.trolle.af.wingman.screen

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberCoroutineScope
import cafe.adriel.voyager.core.lifecycle.LifecycleEffect
import cafe.adriel.voyager.core.screen.Screen
import dev.trolle.af.wingman.service.NavigationService
import dev.trolle.af.wingman.compose.SignIn
import dev.trolle.af.wingman.compose.local.LocalPhoneNumberProvider
import dev.trolle.af.wingman.ext.throwCancellation
import dev.trolle.af.wingman.koin.getScreenModel
import dev.trolle.af.wingman.repository.UserRepository
import dev.trolle.af.wingman.resources.Strings
import dev.trolle.af.wingman.screen.util.StateScreenModel
import dev.trolle.af.wingman.screen.util.launch
import dev.trolle.af.wingman.service.PhoneValidateService
import io.github.aakira.napier.Napier
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

data class SignInState(
    val phoneNumber: String = "",
    val isValid: Boolean? = null,
    val filterInteraction: Boolean = true,
    val isButtonEnabled: Boolean = true,
    val errorMessage: String? = null,
    val isLogoVisible: Boolean = false,
)

internal object SignInScreen : Screen {
    internal class SignInScreenModel(
        private val phoneValidateService: PhoneValidateService,
        private val userRepository: UserRepository,
        private val navigationService: NavigationService,
    ) : StateScreenModel<SignInState>(SignInState()) {

        fun onPhoneNumberChange(number: String?) =
            launch {
                updateState {
                    it.updatePhoneNumber(number ?: "")
                }
            }

        fun onSignIn() = launch(Dispatchers.Default) {
            val state = updateStateAndGet {
                it.copy(isValid = phoneValidateService.isPhoneNumberValid(it.phoneNumber))
            }

            if (state.isValid == true) {
                updateState { it.copy(isButtonEnabled = false) }
                kotlin.runCatching {
                    userRepository.signInRequestOneTimePassword(state.phoneNumber)
                }.throwCancellation()
                    .onFailure {
                        Napier.e("Sign In Error", it)
                        // TODO show error toast
                        updateState { it.copy(errorMessage = Strings.error_something_went_wrong) }
                    }.onSuccess {
                        navigationService.open(OneTimePasswordScreen(state.phoneNumber))
                    }
                updateState { it.copy(isButtonEnabled = true) }
            }
        }


        private fun SignInState.updatePhoneNumber(number: String): SignInState {
            val filteredNumber = number.filter { it.isDigit() || it == '+' }
            val updatedIsValid = when {
                filteredNumber.isEmpty() -> null // reset validation
                isValid == false -> phoneValidateService.isPhoneNumberValid(filteredNumber)
                else -> isValid
            }
            return copy(
                phoneNumber = filteredNumber,
                isValid = updatedIsValid,
                filterInteraction = false,
            )
        }

        fun onStart() {
            // Set once so that logo animation is only done once
            updateState { it.copy(isLogoVisible = true) }
        }
    }

    @Composable
    override fun Content() {
        val viewModel = getScreenModel<SignInScreenModel>()
        val state by viewModel.state.collectAsState()
        LifecycleEffect(onStarted = viewModel::onStart)

        // Consider moving into screenModel instead. (Refactor PhoneNumberProvider into Service)
        val phoneNumberProvider = LocalPhoneNumberProvider.current
        val phoneNumberState = phoneNumberProvider?.phoneNumber?.collectAsState()
        val phoneNumberResult = phoneNumberState?.value
        LaunchedEffect(phoneNumberResult) {
            if (phoneNumberProvider == null) {
                viewModel.onPhoneNumberChange(null)
            } else if (phoneNumberResult != null) {
                viewModel.onPhoneNumberChange(phoneNumberResult.getOrNull())
            }
        }
        val scope = rememberCoroutineScope()

        SignIn(
            state,
            viewModel::onPhoneNumberChange,
            viewModel::onSignIn,
            onPressInteractionConsumed = {
                if (phoneNumberResult == null) {
                    phoneNumberProvider?.run {
                        scope.launch {
                            fetchPhoneNumber()
                        }
                    }
                }
            }
        )
    }
}

