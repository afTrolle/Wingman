package dev.trolle.af.wingman.android.preview

import android.content.Context
import android.content.ContextWrapper
import androidx.activity.ComponentActivity
import androidx.compose.runtime.Composable
import androidx.compose.runtime.SideEffect
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.core.view.WindowCompat
import com.google.accompanist.systemuicontroller.rememberSystemUiController
import dev.trolle.af.wingman.compose.WingManTheme

@Composable
fun PreviewDefaults(content: @Composable () -> Unit) {
    // Turn off the decor fitting system windows, means handling insets manually
    val systemUiController = rememberSystemUiController()
    val window = LocalContext.current.getActivity()!!.window
    SideEffect {
        WindowCompat.setDecorFitsSystemWindows(window, false)
        systemUiController.setSystemBarsColor(
            Color.Transparent,
            darkIcons = true,
            isNavigationBarContrastEnforced = false
        )
    }
    WingManTheme(content)
}

private fun Context.getActivity(): ComponentActivity? = when (this) {
    is ComponentActivity -> this
    is ContextWrapper -> baseContext.getActivity()
    else -> null
}