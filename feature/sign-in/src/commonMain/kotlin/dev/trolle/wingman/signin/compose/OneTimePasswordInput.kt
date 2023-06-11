package dev.trolle.wingman.signin.compose

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.text.ExperimentalTextApi
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.OffsetMapping
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.text.input.TransformedText
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp


@OptIn(ExperimentalTextApi::class)
@Composable
fun OneTimePasswordInput(
    length: Int = 6,
    text: String,
    textStyle: TextStyle = MaterialTheme.typography.bodyLarge,
    onTextChanged: (String) -> Unit,
    modifier: Modifier = Modifier,
    error: Boolean,
) {
    val spacerSize: TextUnit = with(LocalDensity.current) { 24.dp.toSp() }
    var textFieldValue = remember { mutableStateOf(TextFieldValue("")) }

    // TODO Figure out custom layouts to add boxes under the input fields.
    BoxWithConstraints {
        Extracted(spacerSize, textFieldValue, length)
    }
}

@Composable
private fun Extracted(
    spacerSize: TextUnit,
    textFieldValue: MutableState<TextFieldValue>,
    length: Int,
) {
    // Fixed width, draw boxes based on the fixed width,
    // set letter spacing depending on textfieldSize.
    var textFieldValue1 by textFieldValue
    Box {
        BasicTextField(
            value = TextFieldValue(annotatedString(spacerSize, "123456")),
            onValueChange = { },
            enabled = false,
            readOnly = true,
            singleLine = true,
        )
        Box(Modifier.matchParentSize().background(MaterialTheme.colorScheme.surface))
        BasicTextField(
            value = textFieldValue1,
            onValueChange = {
                textFieldValue1 = it
            },
            modifier = Modifier.matchParentSize().background(Color.Cyan),
            keyboardOptions = KeyboardOptions(
                autoCorrect = false,
                keyboardType = KeyboardType.Number,
            ),
            singleLine = true,
            visualTransformation = { string ->
                TransformedText(
                    annotatedString(spacerSize, string.text),
                    object : OffsetMapping {
                        override fun originalToTransformed(offset: Int): Int = when (offset) {
                            0 -> 1
                            length -> offset * 2
                            string.length -> offset * 2 + 1
                            else -> offset * 2
                        }

                        override fun transformedToOriginal(offset: Int): Int {
                            // TODO look into fixing this
                            return when (offset) {
                                0 -> 0
                                1 -> 0
                                else -> offset / 2
                            }
                        }
                    },
                )
            },
        )
    }
}

private fun annotatedString(
    spacerSize: TextUnit,
    textFieldValue: String,
    maxLength: Int = 5,
) = buildAnnotatedString {
    withStyle(SpanStyle(letterSpacing = spacerSize)) {
        append(" ")
        textFieldValue.forEachIndexed { index, c ->
            withStyle(SpanStyle(letterSpacing = 0.sp)) {
                append(c)
            }
            append(" ")
        }
    }
}
