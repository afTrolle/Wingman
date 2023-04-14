package dev.trolle.af.wingman.screen

import androidx.compose.material.Scaffold
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import cafe.adriel.voyager.core.model.StateScreenModel
import cafe.adriel.voyager.core.model.coroutineScope
import cafe.adriel.voyager.core.screen.Screen
import dev.trolle.af.wingman.koin.getScreenModel
import dev.trolle.af.wingman.koin.rememberKoinInject
import dev.trolle.af.wingman.service.PhoneNumberService
import kotlinx.coroutines.launch

object SignInScreen : Screen {

    data class State(
        val phoneNumber: String = ""
    )

    class SignInScreenModel(
       val phoneNumberService: PhoneNumberService
    ) : StateScreenModel<State>(
        State()
    ) {
        // Might be called multiple times if re-opening the same view
        fun onStart() {
            coroutineScope.launch {

            }
        }
    }

    @Composable
    override fun Content() {
        val viewModel = getScreenModel<SignInScreenModel>()
        val phoneNumberService : PhoneNumberService = rememberKoinInject()

        // Trigger on view open (hopefully launches fetching of phone-number)
        phoneNumberService.TriggerPhoneNumberFetch(key)

        val state by viewModel.state.collectAsState()


        Scaffold {
            Text("hello world")


        }
    }
}

