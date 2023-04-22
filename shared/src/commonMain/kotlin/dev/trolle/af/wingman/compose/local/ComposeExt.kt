package dev.trolle.af.wingman.compose.local

import androidx.compose.foundation.interaction.Interaction
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.TextUnit
import kotlinx.coroutines.flow.Flow

expect fun Modifier.navigationBarsPadding(): Modifier

expect fun Modifier.statusBarsPadding(): Modifier

expect fun Modifier.systemBarsPadding(): Modifier

expect fun Modifier.imePadding(): Modifier

@Composable
fun Dp.toSp() = LocalDensity.current.run {
    toSp()
}

@Composable
fun TextUnit.toDp() = LocalDensity.current.run {
    toDp()
}

class FilterableInteractionSource(
    private val filter: (Interaction) -> Interaction?,
) : MutableInteractionSource {
    private val mutableInteractionSource = MutableInteractionSource()

    override val interactions: Flow<Interaction> = mutableInteractionSource.interactions

    override suspend fun emit(interaction: Interaction) {
        val filteredInteraction = filter(interaction)
        if (filteredInteraction != null) {
            mutableInteractionSource.emit(interaction)
        }
    }

    override fun tryEmit(interaction: Interaction): Boolean {
        val filteredInteraction = filter(interaction)
        return if (filteredInteraction != null) {
            mutableInteractionSource.tryEmit(interaction)
        } else {
            true
        }
    }
}
