package dev.trolle.wingman.signin

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import cafe.adriel.voyager.core.screen.Screen
import dev.trolle.wingman.common.ext.runCatchingCancelable
import dev.trolle.wingman.signin.compose.OneTimePasswordLayout
import dev.trolle.wingman.ui.Navigation
import dev.trolle.wingman.ui.StringsContainer
import dev.trolle.wingman.ui.ext.getScreenModel
import dev.trolle.wingman.ui.ext.launch
import dev.trolle.wingman.ui.string.EnStrings
import dev.trolle.wingman.ui.string.StringsDefinition
import dev.trolle.wingman.ui.voyager.StateScreenModel
import dev.trolle.wingman.user.User
import io.github.aakira.napier.Napier
import org.koin.core.parameter.parametersOf

data class OneTimePasswordState(
    val oneTimePasswordLength: Int = 6,
    val oneTimePassword: String = "",
    val isOneTimePasswordError: Boolean = false,
    val signInEnabled: Boolean = true,
    val label: String = EnStrings.email_otp_label,
    val desc: String = EnStrings.email_otp_desc,
    val caption: String = EnStrings.email_otp_caption("a****.com"),
)

val OneTimePasswordState.isCorrectLength get() = oneTimePassword.length == oneTimePasswordLength


private fun createState(config: OneTimePasswordConfig, strings: StringsDefinition) = when (config) {
    // This is not ideal if you change language while the app is running.
    is OneTimePasswordConfig.Email -> OneTimePasswordState(
        label = strings.email_otp_label,
        desc = strings.email_otp_desc,
        caption = strings.email_otp_caption(config.email),
    )

    is OneTimePasswordConfig.Phone -> OneTimePasswordState(
        label = strings.phone_otp_label,
        desc = strings.phone_otp_desc,
        caption = strings.phone_otp_caption.invoke(config.phone),
    )
}

internal class OneTimePasswordModel(
    private val config: OneTimePasswordConfig,
    strings: StringsContainer,
    private val user: User,
    private val navigationService: Navigation,
) : StateScreenModel<OneTimePasswordState>(createState(config, strings.strings)) {

    fun onOneTimePasswordChanged(text: String) = launch {
        updateState {
            val oneTimePassword = text.filter { char -> char.isDigit() }
                .take(it.oneTimePasswordLength)

            val isOneTimePasswordError =
                if (oneTimePassword.isEmpty() && it.isOneTimePasswordError) false else it.isOneTimePasswordError

            it.copy(
                oneTimePassword = oneTimePassword,
                isOneTimePasswordError = isOneTimePasswordError,
            )
        }
    }

    fun onSignIn() = launch {
        val state = updateStateAndGet { it.copy(isOneTimePasswordError = !it.isCorrectLength) }
        if (state.isCorrectLength) {
            updateState { it.copy(signInEnabled = false) }
            runCatchingCancelable {
                when (config) {
                    is OneTimePasswordConfig.Email -> user.verifyEmailOneTimePassword(state.oneTimePassword)
                    is OneTimePasswordConfig.Phone -> user.verifyPhoneOneTimePassword(state.oneTimePassword)
                }
            }.onFailure { error ->
                Napier.w("otp-failed", error)
                updateState { it.copy(isOneTimePasswordError = true) }
                // TODO show error message
            }.onSuccess {
                navigationService.openHomeScreen()
            }
            updateState { it.copy(signInEnabled = true) }
        }
    }
}

data class OneTimePasswordScreen(
    val config: OneTimePasswordConfig,
) : Screen {
    @Composable
    override fun Content() {
        val viewModel = getScreenModel<OneTimePasswordModel> { parametersOf(config) }
        val state by viewModel.state
        OneTimePasswordLayout(
            state,
            viewModel::onOneTimePasswordChanged,
            viewModel::onSignIn,
        )
    }
}
