package dev.trolle.wingman.signin.compose

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusProperties
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.input.key.Key
import androidx.compose.ui.input.key.KeyEvent
import androidx.compose.ui.input.key.key
import androidx.compose.ui.input.key.onKeyEvent
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp


@OptIn(ExperimentalComposeUiApi::class)
@Composable
fun OneTimePasswordInput(
    length: Int = 6,
    onTextChanged: (String) -> Unit,
) {
    var text by rememberSaveable { mutableStateOf("") }
    LaunchedEffect(text) { onTextChanged(text) }
    val focus = remember { List(length) { FocusRequester() } }

    Row(
        horizontalArrangement = Arrangement.spacedBy(8.dp),
    ) {
        repeat(length) { index ->
            val writeableIndex by derivedStateOf { text.length.coerceAtMost(length - 1) }
            val currentFocus = focus[index]
            val boxText = text.getOrNull(index)?.toString() ?: ""
            val onKeyEvent: (KeyEvent) -> Boolean = {
                when (it.key) {
                    Key.Backspace, Key.Delete -> if (text.getOrNull(index) == null) {
                        text = text.dropLast(1)
                        kotlin.runCatching {
                            focus.getOrNull(writeableIndex)?.requestFocus()
                        }
                        true
                    } else {
                        text = text.removeRange(index, index + 1)
                        true
                    }

                    else -> false
                }
        }
        Box(modifier = Modifier.onKeyEvent(onKeyEvent)) {
            BasicTextField(
                modifier = Modifier
                    .focusRequester(currentFocus)
                    .focusProperties {
                        focus.getOrNull(index - 1)?.let { previous = it }
                        focus.getOrNull(index + 1)?.let { next = it }
                    }.onFocusChanged {
                        if (it.isFocused && index > 0 && text.getOrNull(index - 1) == null) {
                            kotlin.runCatching {
                                focus.getOrNull(index - 1)?.requestFocus()
                            }
                        }
                    },
                value = boxText,
                onValueChange = { updatedText: String ->
                    val beforeText = text
                    val firstPart =
                        beforeText.substring(0, index.coerceAtMost(beforeText.length))
                    val secondPart = beforeText.substring(
                        (index + 1).coerceAtMost(beforeText.length),
                        beforeText.length,
                    )
                    val filtered = buildString {
                        append(firstPart)
                        append(updatedText.filter { it.isDigit() })
                        append(secondPart)
                    }.take(length)
                    if (text != filtered) {
                        text = filtered
                        val hasText = filtered.getOrNull(index) == null
                        kotlin.runCatching {
                            when {
                                hasText -> focus.getOrNull(writeableIndex)?.requestFocus()
                                writeableIndex == index + 1 -> focus.getOrNull(writeableIndex)?.requestFocus()
                                else -> Unit
                            }                        }
                    }
                },
                singleLine = true,
                textStyle = MaterialTheme.typography.headlineSmall.copy(
                    textAlign = TextAlign.Center,
                ),
                decorationBox = { content ->
                    Box(
                        Modifier.size(40.dp, 52.dp)
                            .background(
                                MaterialTheme.colorScheme.secondaryContainer,
                                MaterialTheme.shapes.small,
                            ),
                        contentAlignment = Alignment.Center,
                    ) {
                        content()
                    }
                },
            )
        }
    }
}
}