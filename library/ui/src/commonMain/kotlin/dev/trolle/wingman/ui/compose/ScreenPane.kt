package dev.trolle.wingman.ui.compose

import androidx.compose.foundation.layout.BoxScope
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.moriatsushi.insetsx.safeArea
import dev.trolle.wingman.ui.MaterialThemeWingman
import dev.trolle.wingman.ui.ext.coerceAtLeast

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ScreenPane(
    bottomBar: @Composable () -> Unit = {},
    content: @Composable BoxScope.() -> Unit,
) = Scaffold(
    containerColor = MaterialThemeWingman.colorScheme.secondaryContainer,
    contentWindowInsets = WindowInsets.safeArea,
    bottomBar = bottomBar,
) {
    Pane(
        modifier = Modifier.padding(it.coerceAtLeast(horizontal = 16.dp , vertical = 8.dp)),
        innerModifier = Modifier.fillMaxSize(),
        containerColor = MaterialThemeWingman.colorScheme.surface,
        content = content,
    )
}