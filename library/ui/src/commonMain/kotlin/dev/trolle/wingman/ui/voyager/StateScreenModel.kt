package dev.trolle.wingman.ui.voyager

import cafe.adriel.voyager.core.model.ScreenModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.flow.updateAndGet

abstract class StateScreenModel<S>(initialState: S) : ScreenModel {
    private val mutableState: MutableStateFlow<S> = MutableStateFlow(initialState)
    val state: StateFlow<S> = mutableState

    protected fun updateState(update: (state: S) -> S) =
        mutableState.update(update)

    protected fun updateStateAndGet(update: (state: S) -> S): S =
        mutableState.updateAndGet(update)
}
