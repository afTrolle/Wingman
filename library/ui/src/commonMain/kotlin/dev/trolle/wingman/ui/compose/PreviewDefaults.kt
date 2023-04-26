package dev.trolle.wingman.ui.compose

import androidx.compose.runtime.Composable
import dev.trolle.wingman.ui.UiBase

@Composable
fun PreviewDefaults(content: @Composable () -> Unit) =
    UiBase(isPreview = true, content)
