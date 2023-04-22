package dev.trolle.af.wingman.compose.preview

import androidx.compose.runtime.Composable
import dev.trolle.af.wingman.compose.AndroidComponents
import dev.trolle.af.wingman.compose.WingManTheme

@Composable
fun PreviewDefaults(content: @Composable () -> Unit) =
    AndroidComponents {
        WingManTheme(content)
    }
