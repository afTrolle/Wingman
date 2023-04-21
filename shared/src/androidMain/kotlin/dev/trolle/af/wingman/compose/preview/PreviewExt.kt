package dev.trolle.af.wingman.compose.preview

import android.content.Context
import android.content.ContextWrapper
import androidx.activity.ComponentActivity
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.SideEffect
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalDensity
import androidx.core.view.WindowCompat
import com.google.accompanist.systemuicontroller.rememberSystemUiController
import com.seiko.imageloader.ImageLoader
import com.seiko.imageloader.LocalImageLoader
import com.seiko.imageloader.cache.memory.maxSizePercent
import com.seiko.imageloader.component.setupDefaultComponents
import dev.trolle.af.wingman.compose.WingManTheme
import okio.Path.Companion.toOkioPath

@Composable
fun PreviewDefaults(content: @Composable () -> Unit) {
    // Turn off the decor fitting system windows, means handling insets manually
    val systemUiController = rememberSystemUiController()
    val window = LocalContext.current.getActivity()?.window
    SideEffect {
        window?.let { WindowCompat.setDecorFitsSystemWindows(window, false) }
        systemUiController.setSystemBarsColor(
            Color.Transparent,
            darkIcons = true,
            isNavigationBarContrastEnforced = false
        )
    }
    CompositionLocalProvider(LocalImageLoader provides generateImageLoader()) {
        WingManTheme(content)
    }
}

private fun Context.getActivity(): ComponentActivity? = when (this) {
    is ComponentActivity -> this
    is ContextWrapper -> baseContext.getActivity()
    else -> null
}

@Composable
fun generateImageLoader(): ImageLoader {
    val context = LocalContext.current
    val density = LocalDensity.current
    return ImageLoader() {
        components {
            setupDefaultComponents(
                context,
                density
            )
        }
        interceptor {
            memoryCacheConfig {
                // Set the max size to 25% of the app's available memory.
                maxSizePercent(context, 0.25)
            }
            diskCacheConfig {
                directory(context.cacheDir.resolve("image_cache").toOkioPath())
                maxSizeBytes(512L * 1024 * 1024) // 512MB
            }
        }
    }
}