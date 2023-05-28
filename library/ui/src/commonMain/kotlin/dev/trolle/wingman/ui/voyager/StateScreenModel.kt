package dev.trolle.wingman.ui.voyager

import androidx.compose.runtime.MutableState
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import cafe.adriel.voyager.core.model.ScreenModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

abstract class StateScreenModel<S>(initialState: S) : ScreenModel {
    private val mutableState: MutableState<S> = mutableStateOf(initialState)
    val state: State<S> = mutableState

    protected suspend fun updateState(update: (state: S) -> S) =
        mutableState.update(update)

    /***
     * Updates state but it is not thread-safe!
     */
    protected fun updateStateUnSafe(update: (state: S) -> S) =
        mutableState.updateUnSafe(update)

    protected suspend fun updateStateAndGet(update: (state: S) -> S): S =
        mutableState.updateAndGet(update)
}

suspend inline fun <T> MutableState<T>.updateAndGet(function: (T) -> T): T {
    while (true) {
        val prevValue = value
        val nextValue = function(prevValue)
        if (compareAndSet(prevValue, nextValue)) {
            return nextValue
        }
    }
}

suspend inline fun <T> MutableState<T>.update(function: (T) -> T) {
    while (true) {
        val prevValue = value
        val nextValue = function(prevValue)
        if (compareAndSet(prevValue, nextValue)) {
            return
        }
    }
}

// Naive solution, Use main thread to synchronize
suspend fun <T> MutableState<T>.compareAndSet(prevValue: T, nextValue: T): Boolean {
    return withContext(Dispatchers.Main) {
        val currentValue = value
        if (prevValue == currentValue) {
            value = nextValue
            true
        } else false
    }
}

inline fun <T> MutableState<T>.updateUnSafe(function: (T) -> T) {
    while (true) {
        val prevValue = value
        val nextValue = function(prevValue)
        val currentValue = value
        if (prevValue == currentValue) {
            value = nextValue
            return
        }
    }
}