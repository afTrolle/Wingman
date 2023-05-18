package dev.trolle.wingman.home.compose.screen.home

import androidx.compose.runtime.Composable
import androidx.paging.PagingData
import androidx.paging.cachedIn
import androidx.paging.compose.collectAsLazyPagingItems
import androidx.paging.map
import cafe.adriel.voyager.core.model.ScreenModel
import cafe.adriel.voyager.core.model.coroutineScope
import cafe.adriel.voyager.core.screen.Screen
import cafe.adriel.voyager.navigator.LocalNavigator
import cafe.adriel.voyager.navigator.currentOrThrow
import dev.trolle.wingman.home.compose.Home
import dev.trolle.wingman.home.compose.custom.CustomTab
import dev.trolle.wingman.home.compose.screen.prompt.PromptScreen
import dev.trolle.wingman.ui.Navigation
import dev.trolle.wingman.ui.ext.getScreenModel
import dev.trolle.wingman.ui.ext.parentScreenOrThrow
import dev.trolle.wingman.user.User
import dev.trolle.wingman.user.age
import dev.trolle.wingman.user.tinder.model.Match
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.channels.BufferOverflow
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.debounce
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onEach
import kotlin.time.Duration.Companion.milliseconds

object HomeTab : CustomTab {
    @Composable
    override fun Content() {
        // Tabs are nested, using parent to retain viewModel state
        // Note that this means that the view model can be shared between tabs
        val parentScreen = LocalNavigator.currentOrThrow.parentScreenOrThrow
        val viewModel = parentScreen.getScreenModel<HomeScreenModel>()
        val lazyPagingMatches = viewModel.matches.collectAsLazyPagingItems()

        Home(
            lazyPagingMatches,
            viewModel::onMatchItem,
        )
    }
}

data class MatchItem(
    val id: String,
    val name: String,
    val image: String,
    val age: Int? = null,
    val latestMessage: String? = null,
    val latestMessageReceived: Boolean? = null,
)

@OptIn(FlowPreview::class)
internal class HomeScreenModel(
    user: User,
    private val navigation: Navigation,
) : ScreenModel {

    private val openScreen = MutableSharedFlow<Screen>(
        extraBufferCapacity = 1,
        onBufferOverflow = BufferOverflow.DROP_LATEST,
    )

    init {
        openScreen.debounce(100.milliseconds).onEach {
            navigation.open(it)
        }.launchIn(coroutineScope)
    }

    fun onMatchItem(matchItem: MatchItem?) {
        if (matchItem == null) return
        openScreen.tryEmit(PromptScreen(matchItem.id))
    }

    val matches: Flow<PagingData<MatchItem>> =
        user.matchPager().cachedIn(coroutineScope)
            .map { it.map { match -> match.toMatchItem() } }
}


private fun Match.toMatchItem(): MatchItem = MatchItem(
    id = person.id,
    name = person.name ?: "",
    image = person.photos.first().processedFiles.minBy { photo ->
        if (photo.height != null && photo.width != null) photo.height!! * photo.width!! else Int.MAX_VALUE
    }.url!!,
    age = person.age(),
    latestMessage = messages.firstOrNull()?.message,
    latestMessageReceived = messages.firstOrNull()
        ?.let { message -> message.from == person.id },
)