package dev.trolle.wingman.signin.compose

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
import androidx.compose.material3.LocalTextStyle
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextRange
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.unit.dp
import dev.trolle.wingman.ui.ext.dp

@Composable
fun OneTimePasswordInput(
    length: Int,
    text: String,
    onTextChanged: (TextFieldValue) -> Unit,
    modifier: Modifier = Modifier,
    error: Boolean,
) {
    val textFieldValue = remember(text, length) { TextFieldValue(text, TextRange(length)) }
    var highlight by rememberSaveable { mutableStateOf(false) }
    val indexToHighlight by remember(text) {
        derivedStateOf {
            if (highlight) {
                text.length
            } else {
                null
            }
        }
    }
    BasicTextField(
        value = textFieldValue,
        onValueChange = onTextChanged,
        modifier = modifier.onFocusChanged {
            highlight = it.isFocused
        },
        keyboardOptions = KeyboardOptions(
            autoCorrect = false,
            keyboardType = KeyboardType.Number,
        ),
        singleLine = true,
        decorationBox = {
            Row(
                horizontalArrangement = Arrangement.spacedBy(16.dp),
            ) {
                val highlightIndex = indexToHighlight
                repeat(length) { index ->
                    val char = textFieldValue.text.getOrNull(index)?.toString() ?: ""
                    OneTimePasswordEntry(char, error, highlightIndex == index)
                }
            }
        },
    )
}

@Composable
private fun OneTimePasswordEntry(
    char: String,
    error: Boolean,
    highlight: Boolean,
) {
    val targetColor = when {
        highlight -> MaterialTheme.colorScheme.tertiary
        error -> MaterialTheme.colorScheme.error
        char.isEmpty() -> Color.Gray.copy(alpha = .8f)
        else -> MaterialTheme.colorScheme.primary.copy(.8f)
    }

    val color = animateColorAsState(
        targetValue = targetColor,
    )
    val textStyle = MaterialTheme.typography.titleMedium
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(4.dp),
    ) {
        // Char
        Text(
            text = char,
            style = textStyle,
            color = LocalTextStyle.current.color,
        )
        // Highlight bar
        Box(
            Modifier
                .size(textStyle.fontSize.dp + 4.dp, 6.dp)
                .background(color.value, RoundedCornerShape(4.dp)),
        )
    }
}
