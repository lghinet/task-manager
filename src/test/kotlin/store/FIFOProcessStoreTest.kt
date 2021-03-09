package store

import org.junit.jupiter.api.Test
import org.junit.jupiter.api.Assertions.*
import Process

internal class FIFOProcessStoreTest {

    @Test
    fun `add over capacity with same priority should return true`() {
        val store = FIFOProcessStore(3)
        store.add(Process("1", ProcessPriority.Low))
        store.add(Process("2", ProcessPriority.Low))
        store.add(Process("3", ProcessPriority.Low))

        assertTrue(store.add(Process("4", ProcessPriority.Low)))
    }

    @Test
    fun `add over capacity with higher priority should return true`() {
        val store = FIFOProcessStore(3)
        store.add(Process("1", ProcessPriority.Low))
        store.add(Process("2", ProcessPriority.Low))
        store.add(Process("3", ProcessPriority.Low))

        assertTrue(store.add(Process("4", ProcessPriority.Medium)))
    }

    @Test
    fun `remove item`() {
        val store = FIFOProcessStore(3)
        store.add(Process("1", ProcessPriority.Medium))
        store.add(Process("3", ProcessPriority.Low))

        assertTrue(store.remove("3"))
    }

    @Test
    fun `get by pid`() {
        val store = FIFOProcessStore(3)
        store.add(Process("1", ProcessPriority.Medium))
        store.add(Process("2", ProcessPriority.High))
        store.add(Process("3", ProcessPriority.Low))

        assertEquals("3", store.get("3")?.pid)
    }

    @Test
    fun getAll() {
        val store = FIFOProcessStore(3)
        store.add(Process("1", ProcessPriority.Low))
        store.add(Process("2", ProcessPriority.Medium))
        store.add(Process("3", ProcessPriority.Medium))
        store.add(Process("4", ProcessPriority.Medium))
        store.add(Process("5", ProcessPriority.High))
        store.add(Process("6", ProcessPriority.High))

        assertIterableEquals(listOf("4", "5", "6"), store.getAll().map { x -> x.pid })
    }
}