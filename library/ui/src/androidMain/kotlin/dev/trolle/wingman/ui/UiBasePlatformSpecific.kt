package dev.trolle.wingman.ui

import android.content.Context
import android.content.ContextWrapper
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.ReadOnlyComposable
import androidx.compose.runtime.remember
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalDensity
import androidx.core.app.ComponentActivity
import androidx.core.view.WindowCompat
import com.google.accompanist.systemuicontroller.rememberSystemUiController
import com.seiko.imageloader.ImageLoader
import com.seiko.imageloader.LocalImageLoader
import com.seiko.imageloader.cache.memory.maxSizePercent
import com.seiko.imageloader.component.setupDefaultComponents
import okio.Path.Companion.toOkioPath

@Composable
fun HandleSystemBars() {
    val systemUiController = rememberSystemUiController()
    val window = window()
    val darkMode = isSystemInDarkTheme()
    LaunchedEffect(darkMode) {
        window?.let { WindowCompat.setDecorFitsSystemWindows(window, false) }
        systemUiController.setSystemBarsColor(
            Color.Transparent,
            darkIcons = !darkMode,
            isNavigationBarContrastEnforced = false,
        )
    }
}

@ReadOnlyComposable
@Composable
private fun window() = LocalContext.current.getActivity()?.window

@Composable
actual fun UiBasePlatformSpecific(isPreview: Boolean, content: @Composable () -> Unit) {
    HandleSystemBars()

    CompositionLocalProvider(
        LocalImageLoader provides generateImageLoader(isPreview),
        content = content,
    )
}

private fun Context.getActivity(): ComponentActivity? = when (this) {
    is ComponentActivity -> this
    is ContextWrapper -> baseContext.getActivity()
    else -> null
}


@Composable
fun generateImageLoader(isPreview: Boolean): ImageLoader {
    val context = LocalContext.current.applicationContext
    val density = LocalDensity.current

    return remember(density) {
        ImageLoader {
            components {
                setupDefaultComponents(
                    context,
                    density,
                )
            }
//            options.allowInexactSize = true
            interceptor {
                if (!isPreview) {
                    diskCacheConfig {
                        directory(context.cacheDir.resolve("image_cache").toOkioPath())
                        maxSizeBytes(256L * 1024 * 1024) // 128MB
                    }
                    memoryCacheConfig {
                        maxSizePercent(context, 0.50)
                    }
                }
            }
        }
    }
}

