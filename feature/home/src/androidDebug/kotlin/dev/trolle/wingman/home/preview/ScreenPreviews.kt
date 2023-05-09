package dev.trolle.wingman.home.preview

import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.tooling.preview.Preview
import androidx.paging.LoadState
import androidx.paging.LoadStates
import androidx.paging.PagingData
import androidx.paging.compose.collectAsLazyPagingItems
import dev.trolle.wingman.home.HomeState
import dev.trolle.wingman.home.MatchItem
import dev.trolle.wingman.home.compose.Home
import dev.trolle.wingman.ui.compose.PreviewDefaults
import kotlinx.coroutines.flow.flowOf

@Preview(
    showSystemUi = true,
    showBackground = true,
)
@Composable
fun HomePreview() = PreviewDefaults {
    val state = remember {
        mutableStateOf(HomeState(1))
    }

    val temp = remember {
        val item = MatchItem(
            id = "1",
            name = "jon",
            age = 69,
            latestMessage = "hello",
            latestMessageReceived = true,
        )

        flowOf(PagingData.from(
            data = listOf(item,item),
            sourceLoadStates = LoadStates.IDLE,
            mediatorLoadStates = null,
        ))
    }
    val lazyItems = temp.collectAsLazyPagingItems()

    Home(state = state, lazyItems)
}
