package dev.trolle.wingman.home

import androidx.compose.runtime.Composable
import cafe.adriel.voyager.core.screen.Screen
import dev.trolle.wingman.home.compose.HomeBase

object HomeScreen : Screen {
    @Composable
    override fun Content() {
        HomeBase()
    }
}
