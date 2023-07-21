package dev.trolle.wingman.home.preview

import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import androidx.paging.PagingData
import androidx.paging.compose.collectAsLazyPagingItems
import dev.trolle.wingman.home.compose.StartLayout
import dev.trolle.wingman.home.compose.screen.home.MatchItem
import dev.trolle.wingman.ui.compose.ScreenPreviewDefaults
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOf

@Preview(showSystemUi = false, showBackground = true)
@Composable
private fun StartPreview() = ScreenPreviewDefaults {
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
        ),
    )
    val lazyPagingMatches = flow.collectAsLazyPagingItems()
    StartLayout(lazyPagingMatches = lazyPagingMatches)
}

