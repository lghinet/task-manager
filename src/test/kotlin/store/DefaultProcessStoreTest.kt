package store

import org.junit.jupiter.api.Test
import org.junit.jupiter.api.Assertions.*
import Process

internal class DefaultProcessStoreTest {

    @Test
    fun `add over capacity should return false`() {
        val store = DefaultProcessStore(3)
        store.add(Process("1", ProcessPriority.Low))
        store.add(Process("2", ProcessPriority.Low))
        store.add(Process("3", ProcessPriority.Low))

        assertFalse(store.add(Process("4", ProcessPriority.Low)))
    }

    @Test
    fun `add within capacity should return true`() {
        val store = DefaultProcessStore(6)
        store.add(Process("1", ProcessPriority.Low))
        store.add(Process("2", ProcessPriority.Low))
        store.add(Process("3", ProcessPriority.Low))

        assertTrue(store.add(Process("4", ProcessPriority.Medium)))
    }

    @Test
    fun `remove item`() {
        val store = DefaultProcessStore(3)
        store.add(Process("1", ProcessPriority.Medium))
        store.add(Process("3", ProcessPriority.Low))

        assertTrue(store.remove("3"))
    }

    @Test
    fun `get by pid` () {
        val store = DefaultProcessStore(3)
        store.add(Process("1", ProcessPriority.Medium))
        store.add(Process("2", ProcessPriority.High))
        store.add(Process("3", ProcessPriority.Low))

        assertEquals("3", store.get("3")?.pid)
    }

    @Test
    fun getAll() {
        val store = DefaultProcessStore(3)
        store.add(Process("1", ProcessPriority.Low))
        store.add(Process("2", ProcessPriority.Medium))
        store.add(Process("3", ProcessPriority.Medium))

        assertIterableEquals(listOf("1", "2", "3"), store.getAll().map { x -> x.pid })
    }
}