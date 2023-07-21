package dev.trolle.wingman.ui.compose

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxScope
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Surface
import dev.trolle.wingman.ui.WindowHeightSizeClass
import dev.trolle.wingman.ui.WindowWidthSizeClass
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.unit.dp
import dev.trolle.wingman.ui.LocalWindowSizeClass
import dev.trolle.wingman.ui.MaterialThemeWingman


@Composable
fun Pane(
    modifier: Modifier = Modifier,
    innerModifier: Modifier = Modifier,
    shape: Shape = MaterialThemeWingman.shapes.extraLarge,
    containerColor : Color = MaterialThemeWingman.colorScheme.surface,
    content: @Composable BoxScope.() -> Unit,
) {
    Surface(
        shape = shape,
        color = containerColor,
        modifier = modifier,
        content = {
            Box(
                innerModifier,
                contentAlignment = Alignment.Center,
            ) {
                content()
            }
        },
    )
}