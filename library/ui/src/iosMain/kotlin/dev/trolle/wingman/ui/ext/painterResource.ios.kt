package dev.trolle.wingman.ui.ext

import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.painter.Painter
import org.jetbrains.compose.resources.ExperimentalResourceApi

@OptIn(ExperimentalResourceApi::class)
@Composable
actual fun painterResource(res: String): Painter  = org.jetbrains.compose.resources.painterResource(res)


