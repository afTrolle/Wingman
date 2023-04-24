package dev.trolle.af.wingman.compose.local

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.TextUnit

expect fun Modifier.navigationBarsPadding(): Modifier

expect fun Modifier.statusBarsPadding(): Modifier

expect fun Modifier.systemBarsPadding(): Modifier

expect fun Modifier.imePadding(): Modifier

@Composable
fun Dp.toSp() = LocalDensity.current.run {
    toSp()
}

@Composable
fun TextUnit.toDp() = LocalDensity.current.run {
    toDp()
}
