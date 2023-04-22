package dev.trolle.af.wingman.screen.util

import cafe.adriel.voyager.core.model.ScreenModel
import cafe.adriel.voyager.core.model.coroutineScope
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.CoroutineStart
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.flow.updateAndGet
import kotlinx.coroutines.launch
import kotlin.coroutines.CoroutineContext
import kotlin.coroutines.EmptyCoroutineContext

abstract class StateScreenModel<S>(initialState: S) : ScreenModel {
    protected val mutableState: MutableStateFlow<S> = MutableStateFlow(initialState)
    val state: StateFlow<S> = mutableState

    protected fun updateState(update: (state: S) -> S) =
        mutableState.update(update)

    protected fun updateStateAndGet(update: (state: S) -> S): S =
        mutableState.updateAndGet(update)
}

fun ScreenModel.launch(
    context: CoroutineContext = EmptyCoroutineContext,
    start: CoroutineStart = CoroutineStart.DEFAULT,
    block: suspend CoroutineScope.() -> Unit,
) {
    coroutineScope.launch(
        context,
        start,
        block,
    )
}
