package dev.trolle.wingman.ui.compose

import androidx.compose.material3.windowsizeclass.WindowHeightSizeClass
import androidx.compose.material3.windowsizeclass.WindowSizeClass
import androidx.compose.material3.windowsizeclass.WindowWidthSizeClass
import androidx.compose.runtime.Composable
import dev.trolle.wingman.ui.UiBase

@Composable
fun PreviewDefaults(
    defaultSizeClass: WindowSizeClass = WindowSizeClass(
        widthSizeClass = WindowWidthSizeClass.Compact,
        heightSizeClass = WindowHeightSizeClass.Compact,
    ),
    content: @Composable () -> Unit) =
    UiBase(
        isPreview = true,
        defaultSizeClass = defaultSizeClass,
        content,
    )
