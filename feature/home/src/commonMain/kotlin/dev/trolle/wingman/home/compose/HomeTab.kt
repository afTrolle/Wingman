package dev.trolle.wingman.home.compose

import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.paging.PagingData
import androidx.paging.cachedIn
import androidx.paging.compose.collectAsLazyPagingItems
import androidx.paging.map
import cafe.adriel.voyager.core.model.coroutineScope
import dev.trolle.wingman.ui.ext.getScreenModel
import dev.trolle.wingman.ui.voyager.StateScreenModel
import dev.trolle.wingman.user.User
import dev.trolle.wingman.user.age
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

object HomeTab : CustomTab {

    @Composable
    override fun Content() {
        val viewModel = getScreenModel<HomeScreenModel>()
        val state = viewModel.state.collectAsState()
        val lazyPagingMatches = viewModel.matches.collectAsLazyPagingItems()
        Home(state, lazyPagingMatches)
    }

}

data class HomeState(
    val matches: Int,
)

data class MatchItem(
    val id: String,
    val name: String,
    val image: String,
    val age: Int? = null,
    val latestMessage: String? = null,
    val latestMessageReceived: Boolean? = null,
)

internal class HomeScreenModel(
    private val user: User,
) : StateScreenModel<HomeState>(HomeState(1)) {

    val matches: Flow<PagingData<MatchItem>> = user.matchPager().cachedIn(coroutineScope).map {
        it.map { match ->
            match.run {
                MatchItem(
                    id = person.id,
                    name = person.name ?: "",
                    image = person.photos.first().url!!,
                    age = person.age(),
                    latestMessage = messages.firstOrNull()?.message,
                    latestMessageReceived = messages.firstOrNull()
                        ?.let { message -> message.from == person.id },
                )
            }
        }
    }
}