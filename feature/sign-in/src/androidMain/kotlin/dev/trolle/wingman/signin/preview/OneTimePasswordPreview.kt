package dev.trolle.wingman.signin.preview

import android.content.res.Configuration
import androidx.compose.material3.windowsizeclass.WindowHeightSizeClass
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.tooling.preview.Devices
import androidx.compose.ui.tooling.preview.Preview
import dev.trolle.wingman.signin.OneTimePasswordState
import dev.trolle.wingman.signin.compose.OneTimePasswordLayout
import dev.trolle.wingman.ui.compose.PreviewDefaults

val state = OneTimePasswordState(
    phoneNumber = "+46720208",
    oneTimePassword = "",
)

@Preview(showSystemUi = true, showBackground = true)
@Composable
private fun OneTimePreview() = PreviewDefaults {
    val mutableState = remember {
         mutableStateOf(
             OneTimePasswordState(
                 phoneNumber = "+46720208",
                 oneTimePassword = "",
             )
         )
    }
    val state = mutableState.value
    OneTimePasswordLayout(
        oneTimePasswordState = state,
        onOneTimePasswordChanged = {
            mutableState.value = state.copy(
                oneTimePassword = it
            )
        }
    )
}

@Preview(showSystemUi = true, showBackground = true, uiMode = Configuration.UI_MODE_NIGHT_YES)
@Composable
private fun OneTimePreviewDarkMode() = PreviewDefaults {
    OneTimePasswordLayout(
        oneTimePasswordState = state,
    )
}

@Preview(showSystemUi = true, showBackground = true, uiMode = Configuration.UI_MODE_NIGHT_YES, device = Devices.FOLDABLE)
@Composable
private fun OneTimePreviewFoldable() = PreviewDefaults {
    OneTimePasswordLayout(
        oneTimePasswordState = state,
    )
}

@Preview(showSystemUi = true, showBackground = true, uiMode = Configuration.UI_MODE_NIGHT_YES, device = "spec:width=411dp,height=891dp,dpi=420,isRound=false,chinSize=0dp,orientation=landscape")
@Composable
private fun OneTimePreviewFoldableLandscape() = PreviewDefaults(heightSizeClass = WindowHeightSizeClass.Compact) {
    OneTimePasswordLayout(
        oneTimePasswordState = state,
    )
}