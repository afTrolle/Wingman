package dev.trolle.wingman.home

import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import cafe.adriel.voyager.core.model.coroutineScope
import cafe.adriel.voyager.core.screen.Screen
import dev.trolle.wingman.common.ext.runCatchingCancelable
import dev.trolle.wingman.home.compose.Home
import dev.trolle.wingman.ui.ext.getScreenModel
import dev.trolle.wingman.ui.ext.launch
import dev.trolle.wingman.ui.voyager.StateScreenModel
import dev.trolle.wingman.user.UiMatch
import dev.trolle.wingman.user.User
import dev.trolle.wingman.user.model.Match
import io.github.aakira.napier.Napier
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onCompletion
import kotlinx.coroutines.flow.onEach

object HomeScreen : Screen {
    @Composable
    override fun Content() {
        val viewModel = getScreenModel<HomeScreenModel>()
        val state by viewModel.state.collectAsState()
        Home(state)
    }
}

data class HomeState(
    val matches: List<UiMatch> = emptyList(),
)

internal class HomeScreenModel(
    private val user: User,
) : StateScreenModel<HomeState>(HomeState()) {

    init {
        launch {
            user.matchesFlow.catch {
                Napier.e(it) { "failed to fetch matches" }
                emit(emptyList()) // reset state
            }.onEach {matches ->
                updateState { it.copy(matches = matches) }
            }.onCompletion {
                if (it == null) {
                    Napier.d { "fetched Matches" }
                }
            }.launchIn(coroutineScope)
//            runCatchingCancelable {
//                Napier.d { "starting work" }
//                val matches = user.getMatches()
//                updateState { it.copy(matches = matches) }
//            }.onFailure {
//                Napier.e(it) { "failed to fetch matches" }
//            }.onSuccess {
//                Napier.d { "fetched Matches" }
//            }
        }
    }
}
