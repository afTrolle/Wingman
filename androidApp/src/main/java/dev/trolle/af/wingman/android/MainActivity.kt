package dev.trolle.af.wingman.android

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.core.view.WindowCompat
import dev.trolle.af.wingman.MainView

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Turn off the decor fitting system windows, means handling insets manually
        WindowCompat.setDecorFitsSystemWindows(window, false)

        setContent {
            MainView()
        }
    }
}

