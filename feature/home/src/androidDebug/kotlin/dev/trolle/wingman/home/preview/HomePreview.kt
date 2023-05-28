package dev.trolle.wingman.home.preview

import android.content.res.Configuration
import androidx.compose.material3.windowsizeclass.WindowHeightSizeClass
import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Devices
import androidx.compose.ui.tooling.preview.Preview
import androidx.paging.LoadState
import androidx.paging.LoadStates
import androidx.paging.PagingData
import androidx.paging.compose.collectAsLazyPagingItems
import dev.trolle.wingman.home.compose.Home
import dev.trolle.wingman.home.compose.screen.home.MatchItem
import dev.trolle.wingman.ui.compose.PreviewDefaults
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOf

@Preview(showSystemUi = false, showBackground = true)
@Composable
private fun HomePreview() = PreviewDefaults {
    val flow: Flow<PagingData<MatchItem>> = flowOf(
        PagingData.from(
            listOf(
                MatchItem(
                    id = "1",
                    name = "jon",
                    age = 69,
                    latestMessage = "hello",
                    image = "https://cdnstorage.sendbig.com/unreal/female.webp",
                    latestMessageReceived = true,
                ),
            ),
            sourceLoadStates = LoadStates.IDLE.copy(
                append = LoadState.Loading,
            )
        ),
        )
    val lazyPagingMatches = flow.collectAsLazyPagingItems()
    Home(lazyPagingMatches = lazyPagingMatches)
}

@Preview(showSystemUi = true, showBackground = true, uiMode = Configuration.UI_MODE_NIGHT_YES)
@Composable
private fun HomePreviewDarkMode() = PreviewDefaults {

}

@Preview(
    showSystemUi = true,
    showBackground = true,
    uiMode = Configuration.UI_MODE_NIGHT_YES,
    device = Devices.FOLDABLE,
)
@Composable
private fun HomePreviewFoldable() = PreviewDefaults {

}

@Preview(
    showSystemUi = true,
    showBackground = true,
    uiMode = Configuration.UI_MODE_NIGHT_YES,
    device = "spec:width=411dp,height=891dp,dpi=420,isRound=false,chinSize=0dp,orientation=landscape",
)
@Composable
private fun HomePreviewFoldableLandscape() =
    PreviewDefaults(heightSizeClass = WindowHeightSizeClass.Compact) {

    }