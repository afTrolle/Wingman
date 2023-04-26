package dev.trolle.wingman.ui

import androidx.compose.runtime.Composable

@Composable
actual fun UiBasePlatformSpecific(isPreview: Boolean, content: @Composable () -> Unit) {
    content()
}
