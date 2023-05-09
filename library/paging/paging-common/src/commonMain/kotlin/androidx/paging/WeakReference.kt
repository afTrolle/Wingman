package androidx.paging


interface WeakReference<T> {
    fun get(): T?
}

expect inline fun <reified T : Any> WeakReference(reference: T): WeakReference<T>

