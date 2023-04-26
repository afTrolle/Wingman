package dev.trolle.wingman.home

import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import cafe.adriel.voyager.core.screen.Screen
import dev.trolle.wingman.home.compose.Home
import dev.trolle.wingman.ui.ext.getScreenModel
import dev.trolle.wingman.ui.ext.launch
import dev.trolle.wingman.ui.voyager.StateScreenModel
import dev.trolle.wingman.user.User
import dev.trolle.wingman.user.model.Match
import io.github.aakira.napier.Napier

object HomeScreen : Screen {
    @Composable
    override fun Content() {
        val viewModel = getScreenModel<HomeScreenModel>()
        val state by viewModel.state.collectAsState()
        Home(state)
    }
}

data class HomeState(
    val matches: List<Match> = emptyList(),
)

internal class HomeScreenModel(
    private val user: User,
) : StateScreenModel<HomeState>(HomeState()) {

    init {
        launch {
            Napier.d { "starting work" }
            val matches = user.getMatches()
            updateState { it.copy(matches = matches) }
        }
    }
}
