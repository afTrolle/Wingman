package dev.trolle.wingman.ui

import androidx.compose.material.Colors
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Shapes
import androidx.compose.material.Typography
import androidx.compose.material.lightColors
import androidx.compose.runtime.Composable
import androidx.compose.runtime.ReadOnlyComposable
import androidx.compose.ui.graphics.Color

object MaterialThemeWingman {

    val colors: Colors
        @Composable
        @ReadOnlyComposable
        get() = MaterialTheme.colors

    val typography: Typography
        @Composable
        @ReadOnlyComposable
        get() = MaterialTheme.typography

    val shapes: Shapes
        @Composable
        @ReadOnlyComposable
        get() = MaterialTheme.shapes

    object Palette {
        private val red = Color(0xFFf50057)
        private val redVariant = Color(0xFFbb004e)
        private val green = Color(0xFF00796B)
        private val greenVariant = Color(0xFF004D40)
        private val purple = Color(0xFF880e4f)
        private val peach = Color(0xFFff9e80)
        val sand = Color(0xFFFFF7ED)
        private val sandSurface = Color(0xFFffe9c7)
        private val peachLight = Color(0xFFfce6e3)

        val lightThemeColors = lightColors(
            primary = green,
            primaryVariant = greenVariant,
            secondary = red,
            secondaryVariant = redVariant,
            background = Color.White,
            surface = Color.White,
            onPrimary = Color.White,
            onSecondary = Color.Black,
        )
    }
}

@Composable
fun MaterialThemeWingman(content: @Composable () -> Unit) = MaterialTheme(
    colors = MaterialThemeWingman.Palette.lightThemeColors,
    content = content,
)
