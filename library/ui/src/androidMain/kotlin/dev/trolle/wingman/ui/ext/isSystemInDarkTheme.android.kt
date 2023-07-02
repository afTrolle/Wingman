package dev.trolle.wingman.ui.ext

import androidx.compose.runtime.Composable
import androidx.compose.runtime.ReadOnlyComposable

@ReadOnlyComposable
@Composable
actual fun isSystemInDarkTheme(): Boolean = androidx.compose.foundation.isSystemInDarkTheme()
