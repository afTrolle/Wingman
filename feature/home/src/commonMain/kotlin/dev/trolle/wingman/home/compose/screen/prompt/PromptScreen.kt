package dev.trolle.wingman.home.compose.screen.prompt

import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import cafe.adriel.voyager.core.screen.Screen
import dev.trolle.wingman.ui.ext.getScreenModel
import dev.trolle.wingman.ui.voyager.StateScreenModel
import kotlinx.datetime.Clock
import kotlinx.datetime.Instant


data class PromptScreen(val personId: String) : Screen {

    @Composable
    override fun Content() {
        val viewModel = getScreenModel<PromptViewModel>()
        val state by viewModel.state.collectAsState()

//        Prompt(state)
    }

}

sealed class PromptEntry {
    abstract val id: String
    abstract val timestamp: Instant

    data class FromMatch(
        override val id: String = "",
        override val timestamp: Instant = Clock.System.now(),
        val message: String,
        val icon: String,
    ) : PromptEntry()

    data class ToMatch(
        override val id: String = "",
        override val timestamp: Instant = Clock.System.now(),
        val message: String,
    ) : PromptEntry()

    data class FromAi(
        override val id: String = "",
        override val timestamp: Instant = Clock.System.now(),
        val message: String,
        val icon: String,
    ) : PromptEntry()

    data class ToAi(
        override val id: String,
        override val timestamp: Instant,
        val message: String,
    ) : PromptEntry()
}

data class PromptState(
    val entries: List<PromptEntry> = emptyList(),
    val promptField: String = "",
)

class PromptViewModel() : StateScreenModel<PromptState>(
    PromptState(),
) {


}
