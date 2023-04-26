package dev.trolle.wingman.ui

import androidx.compose.runtime.Composable
import cafe.adriel.lyricist.ProvideStrings

@Composable
fun UiBase(isPreview: Boolean = false, content: @Composable () -> Unit) {
    MaterialThemeWingman {
        ProvideStrings {
            UiBasePlatformSpecific(isPreview = isPreview, content = content)
        }
    }
}

@Composable
expect fun UiBasePlatformSpecific(isPreview: Boolean = false, content: @Composable () -> Unit)
