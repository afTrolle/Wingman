@file:JvmName("ComposeExtJvm")

package dev.trolle.af.wingman.compose.local

import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.systemBarsPadding
import androidx.compose.ui.Modifier

actual fun Modifier.navigationBarsPadding(): Modifier {
    return navigationBarsPadding()
}

actual fun Modifier.imePadding(): Modifier {
    return imePadding()
}

actual fun Modifier.statusBarsPadding(): Modifier {
    return statusBarsPadding()
}

actual fun Modifier.systemBarsPadding(): Modifier {
    return systemBarsPadding()
}
