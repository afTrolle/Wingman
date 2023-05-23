package dev.trolle.wingman.ui

import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.text.intl.Locale
import cafe.adriel.lyricist.Lyricist
import cafe.adriel.lyricist.ProvideStrings
import cafe.adriel.lyricist.Strings
import dev.trolle.wingman.ui.string.Locales

@Composable
fun UiBase(isPreview: Boolean = false, content: @Composable () -> Unit) {
    MaterialThemeWingman {
        CustomProvideStrings {
            UiBasePlatformSpecific(isPreview = isPreview, content = content)
        }
    }
}

@Composable
expect fun UiBasePlatformSpecific(isPreview: Boolean = false, content: @Composable () -> Unit)

@Composable
fun CustomProvideStrings(
    content: @Composable () -> Unit,
) {
    val locale: Locale = Locale.current
    val tag = remember(locale) { locale.toLanguageTag() }
    val lyricist = remember(tag) {
        Lyricist(Locales.EN, Strings).apply {
            languageTag = tag
        }
    }
    ProvideStrings(lyricist, content)
}


