package dev.trolle.af.wingman.ext

import kotlinx.coroutines.CancellationException

/*
    runCatching catches coroutine cancellation, to not break structured concurrency we re-throw it
 */

inline fun <R> runCatchingCancelable(block: () -> R): Result<R> =
    runCatching(block).throwCancellation()

inline fun <T> Result<T>.throwCancellation() =
    onFailure {
        if (it is CancellationException) throw it
    }
