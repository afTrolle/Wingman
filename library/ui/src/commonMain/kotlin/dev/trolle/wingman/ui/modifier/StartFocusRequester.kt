package dev.trolle.wingman.ui.modifier

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester

@Composable
fun Modifier.focusOnStart() = focusRequester(focusRequester = StartFocusRequester())

@Composable
fun StartFocusRequester(): FocusRequester {
    val focusRequester = remember { FocusRequester() }
    LaunchedEffect(focusRequester) {
        focusRequester.requestFocus()
    }
    return focusRequester
}
