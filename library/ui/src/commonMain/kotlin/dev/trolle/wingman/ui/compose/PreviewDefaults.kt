package dev.trolle.wingman.ui.compose

import dev.trolle.wingman.ui.WindowHeightSizeClass
import dev.trolle.wingman.ui.WindowSizeClass
import dev.trolle.wingman.ui.WindowWidthSizeClass
import androidx.compose.runtime.Composable
import dev.trolle.wingman.ui.UiBase

@Composable
fun PreviewDefaults(
    widthSizeClass: WindowWidthSizeClass = WindowWidthSizeClass.Compact,
    heightSizeClass: WindowHeightSizeClass = WindowHeightSizeClass.Medium,
    content: @Composable () -> Unit,
) =
    UiBase(
        isPreview = true,
        defaultSizeClass = WindowSizeClass(widthSizeClass, heightSizeClass),
        content,
    )
