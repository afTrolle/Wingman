package dev.trolle.wingman.ui

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.ProvidableCompositionLocal
import androidx.compose.runtime.compositionLocalOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.text.intl.Locale
import cafe.adriel.lyricist.Lyricist
import cafe.adriel.lyricist.ProvideStrings
import cafe.adriel.lyricist.Strings
import dev.trolle.wingman.ui.string.Locales

@Composable
fun UiBase(
    isPreview: Boolean = false,
    defaultSizeClass: WindowSizeClass = WindowSizeClass(
        widthSizeClass = WindowWidthSizeClass.Compact,
        heightSizeClass = WindowHeightSizeClass.Compact,
    ),
    content: @Composable () -> Unit,
) {
    MaterialThemeWingman {
        CustomProvideStrings {
            ProvideWindowSizeClass(isPreview, defaultSizeClass) {
                UiBasePlatformSpecific(isPreview = isPreview, content = content)
            }
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


// This might cause a bit of flickering.
val LocalWindowSizeClass: ProvidableCompositionLocal<WindowSizeClass> =
    compositionLocalOf {
        WindowSizeClass(
            widthSizeClass = WindowWidthSizeClass.Compact,
            heightSizeClass = WindowHeightSizeClass.Medium,
        )
    }

// This is quite the hack, "work-around".
// Couldn't find a nice way to figure out screen size from compose multiplatform.
// So relative low in the root of the view I setup this to check what the max size can be. as a side composable.
// The Use providable to set WindowSizeClass that can be fetched from other views
@Composable
fun ProvideWindowSizeClass(
    isPreview: Boolean,
    defaultSizeClass: WindowSizeClass = WindowSizeClass(
        widthSizeClass = WindowWidthSizeClass.Compact,
        heightSizeClass = WindowHeightSizeClass.Medium,
    ),
    content: @Composable () -> Unit,
) {
    Box {
        val state = rememberSaveable { mutableStateOf(defaultSizeClass) }
        if (!isPreview) {
            BoxWithConstraints(Modifier.fillMaxSize()) {
                val density = LocalDensity.current
                val sizeClass = remember(maxHeight, maxWidth) {
                    val size =
                        with(density) { Size(width = maxWidth.toPx(), height = maxHeight.toPx()) }
                    WindowSizeClass.calculateFromSize(
                        size = size,
                        density = density,
                    )
                }
                state.value = sizeClass
            }
        }
        // This means though that if on config change for example then we will need to fire one extra composition.
        // So we are going to be one composition behind.
        val windowSizeClass = state.value
        CompositionLocalProvider(LocalWindowSizeClass provides windowSizeClass) {
            content()
        }
    }
}
