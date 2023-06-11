package dev.trolle.wingman.signin.preview

import android.content.res.Configuration.UI_MODE_NIGHT_YES
import androidx.compose.material3.windowsizeclass.WindowHeightSizeClass
import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Devices.FOLDABLE
import androidx.compose.ui.tooling.preview.Preview
import dev.trolle.wingman.signin.compose.SignInLayout
import dev.trolle.wingman.ui.compose.PreviewDefaults

@Preview(showSystemUi = true, showBackground = true)
@Composable
private fun SignInPreview() = PreviewDefaults {
    SignInLayout()
}

@Preview(showSystemUi = true, showBackground = true, uiMode = UI_MODE_NIGHT_YES)
@Composable
private fun SignInPreviewDarkMode() = PreviewDefaults {
    SignInLayout()
}

@Preview(showSystemUi = true, showBackground = true, uiMode = UI_MODE_NIGHT_YES, device = FOLDABLE)
@Composable
private fun SignInPreviewFoldable() = PreviewDefaults {
    SignInLayout()
}

@Preview(showSystemUi = true, showBackground = true, uiMode = UI_MODE_NIGHT_YES, device = "spec:width=411dp,height=891dp,dpi=420,isRound=false,chinSize=0dp,orientation=landscape")
@Composable
private fun SignInPreviewFoldableLandscape() = PreviewDefaults(heightSizeClass = WindowHeightSizeClass.Compact) {
    SignInLayout()
}