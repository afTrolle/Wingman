package dev.trolle.wingman.home.compose

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.State
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.sp
import androidx.paging.LoadState
import androidx.paging.compose.LazyPagingItems
import dev.trolle.wingman.home.HomeState
import dev.trolle.wingman.home.MatchItem


@Composable
fun Home(state: State<HomeState>, lazyPagingMatches: LazyPagingItems<MatchItem>) = Surface {

    LazyColumn {
        if (lazyPagingMatches.loadState.refresh == LoadState.Loading) {
            item {
                Text(
                    text = "Waiting for items to load from the backend",
                    modifier = Modifier.fillMaxWidth()
                        .wrapContentWidth(Alignment.CenterHorizontally),
                )
            }
        }

        items(count = lazyPagingMatches.itemCount) { index ->
            val item = lazyPagingMatches[index]
            Text("Index=$index: $item", fontSize = 20.sp)
        }

        if (lazyPagingMatches.loadState.append == LoadState.Loading) {
            item {
                CircularProgressIndicator(
                    modifier = Modifier.fillMaxWidth()
                        .wrapContentWidth(Alignment.CenterHorizontally),
                )
            }
        }
    }
}