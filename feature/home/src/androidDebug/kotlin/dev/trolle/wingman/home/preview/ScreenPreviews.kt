package dev.trolle.wingman.home.preview

import androidx.compose.material.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.tooling.preview.Preview
import androidx.paging.PagingData
import androidx.paging.compose.collectAsLazyPagingItems
import dev.trolle.wingman.home.compose.Home
import dev.trolle.wingman.home.compose.HomeState
import dev.trolle.wingman.home.compose.MatchItem
import dev.trolle.wingman.ui.compose.PreviewDefaults
import kotlinx.coroutines.flow.flowOf

private val item = MatchItem(
    id = "1",
    name = "jon",
    age = 69,
    latestMessage = "hello",
    image = "https://cdnstorage.sendbig.com/unreal/female.webp",
    latestMessageReceived = true,
)

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
        flowOf(
            PagingData.from(
                data = listOf(item, item),
            ),
        )
    }
    val lazyItems = temp.collectAsLazyPagingItems()

    Home(state = state, lazyItems)
}


@Preview(
    showBackground = true,
)
@Composable
fun MatchItemPreview() = PreviewDefaults {
    Surface {
        MatchItem(item)
    }
}

@Preview
@Composable
fun MatchItemLoadingPreview() = PreviewDefaults {
    Surface {
        MatchItem(null)
    }
}
