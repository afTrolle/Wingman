package dev.trolle.af.wingman.screen

import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import cafe.adriel.voyager.core.screen.Screen
import dev.trolle.af.wingman.compose.OneTimePassword
import dev.trolle.af.wingman.koin.getScreenModel
import dev.trolle.af.wingman.screen.util.StateScreenModel
import org.koin.core.parameter.parametersOf

data class OneTimePasswordState(
    val oneTimePassword: String = "",
    val length: Int = 6
)

data class OneTimePasswordScreen(
    val phoneNumber: String
) : Screen {
    class OneTimePasswordModel(
        val phoneNumber: String
    ) : StateScreenModel<OneTimePasswordState>(OneTimePasswordState()) {
        fun onOneTimePasswordChanged(text: String) {
            updateState {
                it.copy(oneTimePassword = text.filter { char -> char.isDigit() }.take(it.length))
            }
        }
    }

    @Composable
    override fun Content() {
        val viewModel = getScreenModel<OneTimePasswordModel> { parametersOf(phoneNumber) }
        val state by viewModel.state.collectAsState()
        OneTimePassword(state, viewModel::onOneTimePasswordChanged)
    }

}
