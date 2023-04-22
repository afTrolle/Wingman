package dev.trolle.af.wingman.screen

import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import cafe.adriel.voyager.core.screen.Screen
import dev.trolle.af.wingman.compose.Home
import dev.trolle.af.wingman.koin.getScreenModel
import dev.trolle.af.wingman.repository.UserRepository
import dev.trolle.af.wingman.screen.util.StateScreenModel
import dev.trolle.af.wingman.screen.util.launch
import io.github.aakira.napier.Napier

internal object HomeScreen : Screen {
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

class HomeScreenModel(
    private val userRepository: UserRepository,
) : StateScreenModel<HomeState>(HomeState()) {

    init {
        launch {
            Napier.d { "starting work" }
            val matches = userRepository.getMatches()
            updateState { it.copy(matches = matches) }
        }
    }
}

data class Match(
    val name: String,
    val age: String,
    val imageUrl: String,
    val opener: String,
)
