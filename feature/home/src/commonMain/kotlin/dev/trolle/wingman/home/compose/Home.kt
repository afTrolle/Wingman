package dev.trolle.wingman.home.compose

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.Divider
import androidx.compose.material3.pullrefresh.PullRefreshIndicator
import androidx.compose.material3.pullrefresh.pullRefresh
import androidx.compose.material3.pullrefresh.rememberPullRefreshState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.paging.LoadState
import androidx.paging.compose.LazyPagingItems
import dev.trolle.wingman.home.compose.screen.home.MatchItem
import dev.trolle.wingman.ui.ext.statusBarsPadding

@Composable
fun StartLayout(
    lazyPagingMatches: LazyPagingItems<MatchItem>,
    onMatchItem: (MatchItem?) -> Unit = {},
) {
    PullToRefreshBox(lazyPagingMatches) {
        LazyColumn {
            item {
                // Append so first item is bellow status-bar
                // Not sure but content padding didn't work for this task.
                Box(Modifier.statusBarsPadding()) {}
            }
            items(count = lazyPagingMatches.itemCount) { index ->
                val item = lazyPagingMatches[index]
                MatchItem(
                    item,
                    Modifier
                        .clickable { onMatchItem(item) }
                        .padding(horizontal = 32.dp, vertical = 16.dp),
                )
                Divider(modifier = Modifier.padding(end = 48.dp))
            }

            if (lazyPagingMatches.loadState.append == LoadState.Loading) item {
                MatchItem(
                    null,
                    modifier = Modifier.padding(horizontal = 32.dp, vertical = 16.dp),
                )
            }
        }
    }
}

@Composable
fun PullToRefreshBox(
    lazyItems: LazyPagingItems<*>,
    modifier: Modifier = Modifier,
    Content: @Composable () -> Unit,
) {
    val refreshing by remember {
        derivedStateOf {
            val loadState = lazyItems.loadState
            loadState.refresh == LoadState.Loading
        }
    }

    val pullRefreshState = rememberPullRefreshState(
        refreshing,
        onRefresh = { lazyItems.refresh() },
    )

    Box(modifier = modifier.fillMaxSize().pullRefresh(pullRefreshState, refreshing.not())) {
        Content()
        PullRefreshIndicator(
            refreshing,
            pullRefreshState,
            Modifier.align(Alignment.TopCenter),
        )
    }
}