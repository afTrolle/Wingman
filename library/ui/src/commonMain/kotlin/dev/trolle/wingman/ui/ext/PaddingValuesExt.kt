package dev.trolle.wingman.ui.ext

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.calculateEndPadding
import androidx.compose.foundation.layout.calculateStartPadding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.ReadOnlyComposable
import androidx.compose.runtime.Stable
import androidx.compose.ui.platform.LocalLayoutDirection
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.coerceAtLeast

@ReadOnlyComposable
@Stable
@Composable
fun PaddingValues.coerceAtLeast(start: Dp, top: Dp, bottom: Dp, end: Dp): PaddingValues {
    val rtl = LocalLayoutDirection.current
    return PaddingValues(
        start = calculateStartPadding(rtl).coerceAtLeast(start),
        top = calculateTopPadding().coerceAtLeast(top),
        bottom = calculateBottomPadding().coerceAtLeast(bottom),
        end = calculateEndPadding(rtl).coerceAtLeast(end),
    )
}

@ReadOnlyComposable
@Stable
@Composable
fun PaddingValues.coerceAtLeast(horizontal: Dp, vertical: Dp): PaddingValues =
    coerceAtLeast(horizontal, vertical, vertical, horizontal)

@ReadOnlyComposable
@Stable
@Composable
fun PaddingValues.coerceAtLeast(dp: Dp): PaddingValues = coerceAtLeast(dp, dp, dp, dp)