package dev.trolle.wingman.ui.ext

import androidx.compose.runtime.Composable
import androidx.compose.runtime.ReadOnlyComposable
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.TextUnit

val Dp.sp
    @Composable
    @ReadOnlyComposable
    get() = LocalDensity.current.run { toSp() }

val TextUnit.dp
    @Composable
    @ReadOnlyComposable
    get() = LocalDensity.current.run { toDp() }
