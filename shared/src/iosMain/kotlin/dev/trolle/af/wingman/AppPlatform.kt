package dev.trolle.af.wingman

import androidx.compose.runtime.Composable

@Composable
internal actual fun AppPlatform(content: @Composable () -> Unit) {
    content()
}