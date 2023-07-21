package dev.trolle.wingman.ui.compose

import androidx.compose.foundation.layout.BoxScope
import androidx.compose.runtime.Composable
import dev.trolle.wingman.ui.UiBase
import dev.trolle.wingman.ui.WindowHeightSizeClass
import dev.trolle.wingman.ui.WindowSizeClass
import dev.trolle.wingman.ui.WindowWidthSizeClass

@Composable
fun PreviewDefaults(
    widthSizeClass: WindowWidthSizeClass = WindowWidthSizeClass.Compact,
    heightSizeClass: WindowHeightSizeClass = WindowHeightSizeClass.Medium,
    content: @Composable () -> Unit,
) = UiBase(
    isPreview = true,
    defaultSizeClass = WindowSizeClass(widthSizeClass, heightSizeClass),
    content,
)

@Composable
fun ScreenPreviewDefaults(
    widthSizeClass: WindowWidthSizeClass = WindowWidthSizeClass.Compact,
    heightSizeClass: WindowHeightSizeClass = WindowHeightSizeClass.Medium,
    content: @Composable BoxScope.() -> Unit,
) = UiBase(
    isPreview = true,
    defaultSizeClass = WindowSizeClass(widthSizeClass, heightSizeClass),
) {
    ScreenPane(content = content)
}
