package dev.trolle.af.wingman.compose

import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material.Button
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp


@Composable
fun BigButton(
    text: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
    interactionSource: MutableInteractionSource = remember { MutableInteractionSource() },
) = Button(
    onClick = onClick,
    modifier = modifier.fillMaxWidth(),
    enabled = enabled,
    interactionSource = interactionSource,
    contentPadding = PaddingValues(horizontal = 4.dp),
) {
    Text(
        text,
        fontSize = 18.sp
    )
}

