package dev.trolle.wingman.ui.compose

import androidx.compose.material3.windowsizeclass.WindowHeightSizeClass
import androidx.compose.material3.windowsizeclass.WindowSizeClass
import androidx.compose.material3.windowsizeclass.WindowWidthSizeClass
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
