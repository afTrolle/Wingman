package dev.trolle.af.wingman.screen

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberCoroutineScope
import cafe.adriel.voyager.core.model.coroutineScope
import cafe.adriel.voyager.core.screen.Screen
import dev.trolle.af.wingman.compose.SignIn
import dev.trolle.af.wingman.compose.local.LocalPhoneNumberProvider
import dev.trolle.af.wingman.ext.runCatchingCancelable
import dev.trolle.af.wingman.ext.throwCancellation
import dev.trolle.af.wingman.koin.getScreenModel
import dev.trolle.af.wingman.repository.UserRepository
import dev.trolle.af.wingman.screen.util.StateScreenModel
import dev.trolle.af.wingman.screen.util.launch
import dev.trolle.af.wingman.service.PhoneValidateService
import kotlinx.coroutines.flow.updateAndGet
import kotlinx.coroutines.launch

data class SignInState(
    val phoneNumber: String = "",
    val isValid: Boolean? = null,
    val filterInteraction: Boolean = true,
)

object SignInScreen : Screen {
    class SignInScreenModel(
        private val phoneValidateService: PhoneValidateService,
        private val userRepository: UserRepository,
    ) : StateScreenModel<SignInState>(SignInState()) {

        fun onPhoneNumberChange(number: String) =
            updateState {
                it.updatePhoneNumber(number)
            }

        fun onSignIn() = launch {
            val state = updateStateAndGet {
                it.copy(isValid = phoneValidateService.isPhoneNumberValid(it.phoneNumber))
            }

            if (state.isValid == true) {
                // TODO add loading spinner
                kotlin.runCatching {
                    userRepository.startSignIn(state.phoneNumber)
                }.throwCancellation()
                    .onFailure {
                        // TODO show error toast
                    }.onSuccess {
                        // TODO navigate to openScreen
                    }
                // TODO remove loading spinner

                // TODO Navigate to otp screen
            }
        }

        fun setPhoneNumber(number: String?) {
            val numberToUpdate = number ?: ""
            updateState { state ->
                if (state.phoneNumber.isBlank()) {
                    state.updatePhoneNumber(numberToUpdate)
                        .copy(filterInteraction = false)
                } else {
                    state
                }
            }
        }

        private fun SignInState.updatePhoneNumber(number: String): SignInState {
            val updatedIsValid = when {
                number.isEmpty() -> null // reset validation
                isValid == false -> phoneValidateService.isPhoneNumberValid(number)
                else -> isValid
            }
            return copy(
                phoneNumber = number,
                isValid = updatedIsValid
            )
        }
    }

    @Composable
    override fun Content() {
        val viewModel = getScreenModel<SignInScreenModel>()
        val state by viewModel.state.collectAsState()

        // Consider moving into screenModel instead. (Refactor PhoneNumberProvider into Service)
        val phoneNumberProvider = LocalPhoneNumberProvider.current
        val phoneNumberState = phoneNumberProvider?.phoneNumber?.collectAsState()
        val phoneNumberResult = phoneNumberState?.value
        LaunchedEffect(phoneNumberResult) {
            if (phoneNumberResult != null) {
                viewModel.setPhoneNumber(phoneNumberResult.getOrNull())
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
                } else {
                    viewModel.setPhoneNumber(null)
                }
            })
    }
}

