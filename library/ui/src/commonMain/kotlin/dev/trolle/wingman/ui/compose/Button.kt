package dev.trolle.wingman.ui.compose

import androidx.compose.foundation.layout.defaultMinSize
import androidx.compose.material3.FilledTonalButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import dev.trolle.wingman.ui.MaterialThemeWingman

@Composable
fun BigButton(
    text: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
) = FilledTonalButton(
    modifier = modifier.defaultMinSize(minHeight = 52.dp),
    onClick = onClick,
    enabled = enabled,
    shape = MaterialThemeWingman.shapes.large,
) {
    Text(text = text)
}
