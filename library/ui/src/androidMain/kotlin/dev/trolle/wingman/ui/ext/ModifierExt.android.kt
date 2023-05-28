package dev.trolle.wingman.ui.ext

import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.systemBarsPadding
import androidx.compose.ui.Modifier

actual fun Modifier.navigationBarsPadding(): Modifier = navigationBarsPadding()

actual fun Modifier.imePadding(): Modifier = imePadding()

actual fun Modifier.statusBarsPadding(): Modifier = statusBarsPadding()

actual fun Modifier.systemBarsPadding(): Modifier = systemBarsPadding()
