package dev.trolle.af.wingman

import androidx.compose.runtime.Composable

actual fun getPlatformName(): String = "Android"

@Composable
fun MainView() = App()