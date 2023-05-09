package androidx.paging.internal

import kotlinx.atomicfu.atomic
import kotlinx.atomicfu.update

class CopyOnWriteArrayList<E>(list: List<E> = emptyList()) : MutableList<E> {

    private val atomic = atomic(list)

    private fun mutate(update: MutableList<E>.() -> Unit) =
        atomic.update {
            it.toMutableList().apply(update)
        }

    private fun <R> mutateWithResult(update: MutableList<E>.() -> R): R {
        var result: R? = null
        mutate {
            result = update(this)
        }
        return result!!
    }

    override val size: Int get() = atomic.value.size

    override fun clear() {
        atomic.value = emptyList()
    }

    override fun addAll(elements: Collection<E>): Boolean = mutateWithResult {
        addAll(elements)
    }

    override fun addAll(index: Int, elements: Collection<E>): Boolean = mutateWithResult {
        addAll(index, elements)
    }

    override fun add(index: Int, element: E) = mutate {
        add(index, element)
    }

    override fun add(element: E): Boolean = mutateWithResult {
        add(element)
    }

    override fun get(index: Int): E = atomic.value[index]

    override fun isEmpty(): Boolean = atomic.value.isEmpty()

    override fun iterator(): MutableIterator<E> = internalListIterator()

    override fun listIterator(): MutableListIterator<E> = internalListIterator()

    override fun listIterator(index: Int): MutableListIterator<E> = internalListIterator(index)

    override fun removeAt(index: Int): E = mutateWithResult { removeAt(index) }

    override fun subList(fromIndex: Int, toIndex: Int): MutableList<E> =
        mutateWithResult { subList(fromIndex, toIndex) }

    override fun set(index: Int, element: E): E = mutateWithResult { set(index, element) }

    override fun retainAll(elements: Collection<E>): Boolean =
        mutateWithResult { retainAll(elements) }

    override fun removeAll(elements: Collection<E>): Boolean =
        mutateWithResult { removeAll(elements) }

    override fun remove(element: E): Boolean = mutateWithResult { remove(element) }

    override fun lastIndexOf(element: E): Int = atomic.value.lastIndexOf(element)

    override fun indexOf(element: E): Int = atomic.value.indexOf(element)

    override fun containsAll(elements: Collection<E>): Boolean = atomic.value.containsAll(elements)

    override fun contains(element: E): Boolean = atomic.value.contains(element)

    // No support for chaning state.
    private fun internalListIterator(index: Int? = null) = object : MutableListIterator<E> {
        private val snapshot = atomic.value.toMutableList()
        private val iterator: MutableListIterator<E> =
            if (index == null) snapshot.listIterator() else snapshot.listIterator(index)

        override fun add(element: E) {
            throw UnsupportedOperationException("Can't mutate state of a snapshot")
        }

        override fun remove() {
            throw UnsupportedOperationException("Can't mutate state of a snapshot")
        }

        override fun set(element: E) {
            throw UnsupportedOperationException("Can't mutate state of a snapshot")
        }

        override fun hasNext(): Boolean = iterator.hasNext()
        override fun next(): E = iterator.next()
        override fun hasPrevious(): Boolean = iterator.hasPrevious()
        override fun nextIndex(): Int = iterator.nextIndex()
        override fun previous(): E = iterator.previous()
        override fun previousIndex(): Int = iterator.previousIndex()
    }

}