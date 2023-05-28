package dev.trolle.wingman.ui.compose

import androidx.compose.material3.windowsizeclass.WindowHeightSizeClass
import androidx.compose.runtime.Composable
import dev.trolle.wingman.ui.LocalWindowSizeClass

@Composable
fun LayoutOnDifferentHeight(
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