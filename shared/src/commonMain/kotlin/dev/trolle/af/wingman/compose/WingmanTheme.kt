package dev.trolle.af.wingman.compose

import androidx.compose.material.MaterialTheme
import androidx.compose.material.lightColors
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

object WingManThemeColors {
    private val red = Color(0xFFf50057)
    private val redVariant = Color(0xFFbb004e)
    private val green = Color(0xFF26a69a)
    private val greenVariant = Color(0xFF00796a)
    private val purple = Color(0xFF880e4f)
    private val peach = Color(0xFFff9e80)
    private val peachLight = Color(0xFFfce6e3)

    fun getLightThemeColors() = lightColors(
        primary = green,
        primaryVariant = greenVariant,
        secondary = red,
        secondaryVariant = redVariant,
        background = peachLight,
        surface = peachLight,
        onPrimary = Color.Black,
        onSecondary = Color.Black,
    )
}

@Composable
fun WingManTheme(content: @Composable () -> Unit) {
    MaterialTheme(
        colors = WingManThemeColors.getLightThemeColors(), content = content
    )
}