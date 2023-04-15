package dev.trolle.af.wingman.screen.util;

import cafe.adriel.voyager.core.model.ScreenModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update

abstract class StateScreenModel<S>(initialState: S) : ScreenModel {
    protected val mutableState: MutableStateFlow<S> = MutableStateFlow(initialState)
    val state: StateFlow<S> = mutableState

    protected fun updateState(update: (state: S) -> S) {
        mutableState.update(update)
    }
}