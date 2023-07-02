package dev.trolle.wingman.signin.compose

import dev.trolle.wingman.ui.WindowHeightSizeClass
import androidx.compose.runtime.Composable
import dev.trolle.wingman.ui.LocalWindowSizeClass

@Composable
internal fun LayoutOnDifferentHeights(
    upTo: @Composable () -> Unit,
    over: @Composable () -> Unit,
    heightSizeClass: WindowHeightSizeClass = WindowHeightSizeClass.Compact,
) {
    val currentHeightClass = LocalWindowSizeClass.current.heightSizeClass
    if (currentHeightClass <= heightSizeClass) {
        upTo()
    } else {
        over()
    }
}