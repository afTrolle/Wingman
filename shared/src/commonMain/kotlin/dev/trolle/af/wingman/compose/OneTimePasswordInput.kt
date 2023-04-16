package dev.trolle.af.wingman.compose

import androidx.compose.animation.animateColorAsState
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextRange
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.unit.dp
import dev.trolle.af.wingman.compose.local.toDp


@Composable
fun OneTimePasswordInput(
    length: Int,
    text: String,
    onTextChanged: (TextFieldValue) -> Unit,
    modifier: Modifier = Modifier,
) {
    val textFieldValue = remember(text, length) { TextFieldValue(text, TextRange(length)) }
    BasicTextField(
        value = textFieldValue,
        onValueChange = onTextChanged,
        modifier = modifier,
        keyboardOptions = KeyboardOptions(
            autoCorrect = false,
            keyboardType = KeyboardType.Number
        ),
        singleLine = true,
        decorationBox = {
            Row(
                horizontalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                repeat(length) { index ->
                    val char = textFieldValue.text.getOrNull(index)?.toString() ?: ""
                    OneTimePasswordEntry(char)
                }
            }
        }
    )
}

@Composable
private fun OneTimePasswordEntry(char: String) {
    val color = animateColorAsState(
        targetValue = if (char.isEmpty()) Color.Gray.copy(alpha = .8f)
        else MaterialTheme.colors.primary.copy(.8f)
    )
    val textStyle = MaterialTheme.typography.h6
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(4.dp)
    ) {
        // Char
        Text(
            text = char,
            style = textStyle,
            color = MaterialTheme.colors.onSurface
        )
        // Highlight bar
        Box(
            Modifier
                .size(textStyle.fontSize.toDp() + 4.dp, 6.dp)
                .background(color.value, RoundedCornerShape(4.dp))
        )
    }
}