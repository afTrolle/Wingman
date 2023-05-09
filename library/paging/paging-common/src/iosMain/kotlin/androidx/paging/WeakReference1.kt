package androidx.paging

actual inline fun <reified T: Any> WeakReference(reference: T): WeakReference<T> =
    object : WeakReference<T> {
        val weakReference = kotlin.native.ref.WeakReference(reference)
        override fun get(): T? {
            return weakReference.get()
        }
    }
