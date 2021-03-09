package store

import org.junit.jupiter.api.Test
import org.junit.jupiter.api.Assertions.*
import Process

internal class PriorityProcessStoreTest {

    @Test
    fun `add processes with same priority should return false`() {
        val store = PriorityProcessStore(3)
        store.add(Process("1", ProcessPriority.Low))
        Thread.sleep(1)// add delay between process creations to preserve order
        store.add(Process("2", ProcessPriority.Low))
        Thread.sleep(1)
        store.add(Process("3", ProcessPriority.Low))
        Thread.sleep(1)

        assertFalse(store.add(Process("4", ProcessPriority.Low)))
    }

    @Test
    fun `add higher priority should return true`() {
        val store = PriorityProcessStore(3)
        store.add(Process("1", ProcessPriority.Low))
        Thread.sleep(1)// add delay between process creations to preserve order
        store.add(Process("2", ProcessPriority.Low))
        Thread.sleep(1)
        store.add(Process("3", ProcessPriority.Low))
        Thread.sleep(1)

        assertTrue(store.add(Process("4", ProcessPriority.Medium)))
    }

    @Test
    fun `add lower priority should return false`() {
        val store = PriorityProcessStore(3)
        store.add(Process("1", ProcessPriority.Medium))
        Thread.sleep(1) // add delay between process creations to preserve order
        store.add(Process("2", ProcessPriority.High))
        Thread.sleep(1)
        store.add(Process("3", ProcessPriority.Low))
        Thread.sleep(1)

        assertFalse(store.add(Process("4", ProcessPriority.Low)))
    }

    @Test
    fun `add higher priority should return true mixed priorities`() {
        val store = PriorityProcessStore(3)
        store.add(Process("1", ProcessPriority.Medium))
        Thread.sleep(1) // add delay between process creations to preserve order
        store.add(Process("2", ProcessPriority.High))
        Thread.sleep(1)
        store.add(Process("3", ProcessPriority.Low))
        Thread.sleep(1)

        assertTrue(store.add(Process("4", ProcessPriority.Medium)))
    }

    @Test
    fun `remove item`() {
        val store = PriorityProcessStore(3)
        store.add(Process("1", ProcessPriority.Medium))
        store.add(Process("2", ProcessPriority.High))
        store.add(Process("3", ProcessPriority.Low))

        assertTrue(store.remove("3"))
    }

    @Test
    fun get() {
        val store = PriorityProcessStore(3)
        store.add(Process("1", ProcessPriority.Medium))
        Thread.sleep(1) // add delay between process creations to preserve order
        store.add(Process("2", ProcessPriority.High))
        Thread.sleep(1)
        store.add(Process("3", ProcessPriority.Low))

        assertEquals("3", store.get("3")?.pid)
    }

    @Test
    fun getAll() {
        val store = PriorityProcessStore(3)
        store.add(Process("1", ProcessPriority.Low))
        Thread.sleep(1) // add delay between process creations to preserve order
        store.add(Process("2", ProcessPriority.Medium))
        Thread.sleep(1)
        store.add(Process("3", ProcessPriority.Medium))
        Thread.sleep(1)
        store.add(Process("4", ProcessPriority.Medium))
        Thread.sleep(1)
        store.add(Process("5", ProcessPriority.High))
        Thread.sleep(1)
        store.add(Process("6", ProcessPriority.High))

        assertIterableEquals(listOf("4", "5", "6"), store.getAll().map { x -> x.pid }.sorted())
    }

    @Test
    fun `getAll bigger list`() {
        val store = PriorityProcessStore(4)
        store.add(Process("1", ProcessPriority.High))
        Thread.sleep(1) // add delay between process creations to preserve order
        store.add(Process("2", ProcessPriority.Medium))
        Thread.sleep(1)
        store.add(Process("3", ProcessPriority.Low))
        Thread.sleep(1)
        store.add(Process("4", ProcessPriority.Medium))
        Thread.sleep(1)
        store.add(Process("5", ProcessPriority.Medium))
        Thread.sleep(1)
        store.add(Process("6", ProcessPriority.High))

        assertIterableEquals(listOf("1", "4", "5", "6"), store.getAll().map { x -> x.pid }.sorted())
    }
}