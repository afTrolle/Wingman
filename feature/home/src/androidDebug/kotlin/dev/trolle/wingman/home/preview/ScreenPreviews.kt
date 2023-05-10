package dev.trolle.wingman.home.preview

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.ContentAlpha
import androidx.compose.material.LocalContentAlpha
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.paging.PagingData
import androidx.paging.compose.collectAsLazyPagingItems
import com.seiko.imageloader.rememberAsyncImagePainter
import dev.trolle.wingman.home.compose.Home
import dev.trolle.wingman.home.compose.HomeState
import dev.trolle.wingman.home.compose.MatchItem
import dev.trolle.wingman.ui.MaterialThemeWingman
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

@Composable
fun MatchItem(item: MatchItem) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Image(
            painter = rememberAsyncImagePainter(item.name),
            contentDescription = null,
            contentScale = ContentScale.Crop,
            modifier = Modifier
                .size(64.dp)
                .padding(8.dp)
                .clip(RoundedCornerShape(16.dp)),
        )

        Column(
            verticalArrangement = Arrangement.Center,
        ) {
            Text(
                text = item.name,
                style = MaterialThemeWingman.typography.subtitle1,
                maxLines = 1,
            )
            CompositionLocalProvider(LocalContentAlpha provides ContentAlpha.medium) {
                Text(
                    text = item.latestMessage ?: "",
                    style = MaterialThemeWingman.typography.subtitle2,
                    maxLines = 1,
                )
            }
        }
    }
}
