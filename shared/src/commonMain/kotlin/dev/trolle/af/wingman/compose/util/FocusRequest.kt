package dev.trolle.af.wingman.compose.util

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester

@Composable
fun Modifier.focusOnStart() = focusRequester(focusRequester = StartFocusRequester())

@Composable
fun StartFocusRequester(): FocusRequester {
    val focusRequester = FocusRequester()
    LaunchedEffect(Unit) {
        focusRequester.requestFocus()
    }
    return focusRequester
}
