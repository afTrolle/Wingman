package dev.trolle.af.wingman.screen

import androidx.compose.runtime.Composable
import cafe.adriel.voyager.core.model.ScreenModel
import cafe.adriel.voyager.core.model.rememberScreenModel
import cafe.adriel.voyager.core.screen.Screen

object OTPScreen : Screen {
    @Composable
    override fun Content() {
//        val screenModel = rememberScreenModel<HomeScreenModel>()
    }
}

class OTPScreenModel : ScreenModel {
    // ...
}