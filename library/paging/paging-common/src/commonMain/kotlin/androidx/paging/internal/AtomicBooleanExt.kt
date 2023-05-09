package androidx.paging.internal

import kotlinx.atomicfu.AtomicBoolean

fun AtomicBoolean.get() = value
fun AtomicBoolean.set(boolean: Boolean) {
    value = boolean
}