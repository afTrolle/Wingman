package dev.trolle.wingman.home.compose.custom

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.selection.selectableGroup
import androidx.compose.material3.BottomAppBarDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.contentColorFor
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp

@Composable
fun CustomBottomNavigation(
    modifier: Modifier = Modifier,
    rowModifier : Modifier = Modifier,
    backgroundColor: Color = MaterialTheme.colorScheme.surface,
    contentColor: Color = contentColorFor(backgroundColor),
    elevation: Dp = BottomAppBarDefaults.ContainerElevation,
    content: @Composable RowScope.() -> Unit
) {
    Surface(
        color = backgroundColor,
        contentColor = contentColor,
        tonalElevation = elevation,
        modifier = modifier
    ) {
        Row(
            rowModifier
                .fillMaxWidth()
                .height(56.dp)
                .selectableGroup(),
            horizontalArrangement = Arrangement.SpaceBetween,
            content = content
        )
    }
}