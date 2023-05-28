package dev.trolle.wingman.ui.ext

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Modifier
import androidx.compose.ui.composed
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.platform.LocalFocusManager

expect fun Modifier.navigationBarsPadding(): Modifier

expect fun Modifier.statusBarsPadding(): Modifier

expect fun Modifier.systemBarsPadding(): Modifier

expect fun Modifier.imePadding(): Modifier


@Composable
fun Modifier.requestFocus(
    requestFocus: Boolean,
): Modifier {
    val focusRequester = remember { FocusRequester() }
    val request = rememberSaveable(requestFocus) { mutableStateOf(requestFocus) }
    val requestValue = request.value
    LaunchedEffect(requestValue) {
        if (requestValue) {
            focusRequester.requestFocus()
            request.value = false
        }
    }
    return focusRequester(focusRequester)
}

fun Modifier.captureFocus(
    captureFocus: Boolean,
    onFocusCaptured: () -> Unit,
): Modifier = composed {
    val focusManager = LocalFocusManager.current
    onFocusChanged {
        if (captureFocus && it.isFocused) {
            focusManager.clearFocus()
            onFocusCaptured()
        }
    }
}

