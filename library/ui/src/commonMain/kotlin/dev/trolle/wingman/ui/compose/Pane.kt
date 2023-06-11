package dev.trolle.wingman.ui.compose

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxScope
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Surface
import androidx.compose.material3.windowsizeclass.WindowHeightSizeClass
import androidx.compose.material3.windowsizeclass.WindowWidthSizeClass
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.unit.dp
import dev.trolle.wingman.ui.LocalWindowSizeClass
import dev.trolle.wingman.ui.MaterialThemeWingman

private val paneModifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp)
private val innerPaneModifier = Modifier.fillMaxSize().padding(16.dp)

@Composable
fun Pane(
    modifier: Modifier = paneModifier,
    innerModifier: Modifier = innerPaneModifier,
    shape: Shape = MaterialThemeWingman.shapes.extraLarge,
    containerColor : Color = MaterialThemeWingman.colorScheme.surface,
    cutout: Boolean = false,
    content: @Composable BoxScope.() -> Unit,
) {
    val size = LocalWindowSizeClass.current
    val horizontalPadding = if (size.widthSizeClass <= WindowWidthSizeClass.Compact) 16.dp else 8.dp
    val verticalPadding = if (size.heightSizeClass <= WindowHeightSizeClass.Compact) 4.dp else 16.dp
    val shapePadding = if (cutout) {
        modifier.padding(start = horizontalPadding, top = verticalPadding)
    } else {
        modifier.padding(horizontal = horizontalPadding, vertical = verticalPadding)
    }

    Surface(
        shape = shape,
        color = containerColor,
        modifier = shapePadding,
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