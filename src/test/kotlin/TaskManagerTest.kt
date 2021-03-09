import org.junit.jupiter.api.Test
import org.junit.jupiter.api.Assertions.*

internal class TaskManagerTest {

    @Test
    fun `add over capacity with default store should return false`() {
        val tm = TaskManager(3, TaskManagerStoreStrategy.Default)
        tm.add(Process("1", ProcessPriority.Low))
        tm.add(Process("2", ProcessPriority.Low))
        tm.add(Process("3", ProcessPriority.Low))

        assertFalse(tm.add(Process("4", ProcessPriority.Low)))
    }

    @Test
    fun `add over capacity with fifo store should return true`() {
        val tm = TaskManager(3, TaskManagerStoreStrategy.FIFO)
        tm.add(Process("1", ProcessPriority.Low))
        tm.add(Process("2", ProcessPriority.Low))
        tm.add(Process("3", ProcessPriority.Low))

        assertTrue(tm.add(Process("4", ProcessPriority.Low)))
    }

    @Test
    fun `add over capacity with priority store should return true`() {
        val tm = TaskManager(3, TaskManagerStoreStrategy.Priority)
        tm.add(Process("1", ProcessPriority.Low))
        tm.add(Process("2", ProcessPriority.Low))
        tm.add(Process("3", ProcessPriority.Low))

        assertTrue(tm.add(Process("4", ProcessPriority.Medium)))
    }

    @Test
    fun `add over capacity with priority store should return false`() {
        val tm = TaskManager(3, TaskManagerStoreStrategy.Priority)
        tm.add(Process("1", ProcessPriority.Medium))
        tm.add(Process("2", ProcessPriority.High))
        tm.add(Process("3", ProcessPriority.Medium))

        assertFalse(tm.add(Process("4", ProcessPriority.Medium)))
    }

    @Test
    fun `list sorted by pid`() {
        val tm = TaskManager(3, TaskManagerStoreStrategy.Priority)
        tm.add(Process("1", ProcessPriority.Medium))
        tm.add(Process("2", ProcessPriority.High))
        tm.add(Process("3", ProcessPriority.Medium))
        tm.add(Process("4", ProcessPriority.High))

        assertIterableEquals(listOf("2", "3", "4"), tm.list(SortBy.PID).map { x -> x.pid })
    }

    @Test
    fun `list sorted by CreationDate`() {
        val tm = TaskManager(3, TaskManagerStoreStrategy.Priority)
        tm.add(Process("1", ProcessPriority.Medium))
        Thread.sleep(1)// add delay between process creations to preserve order
        tm.add(Process("2", ProcessPriority.High))
        Thread.sleep(1)
        tm.add(Process("3", ProcessPriority.Low))
        Thread.sleep(1)
        tm.add(Process("4", ProcessPriority.Medium))
        Thread.sleep(1)
        tm.add(Process("5", ProcessPriority.High))

        assertIterableEquals(listOf("2", "4", "5"), tm.list(SortBy.CreationDate).map { x -> x.pid })
    }

    @Test
    fun `list sorted by Priority`() {
        val tm = TaskManager(3, TaskManagerStoreStrategy.Priority)
        tm.add(Process("1", ProcessPriority.Medium))
        tm.add(Process("2", ProcessPriority.High))
        tm.add(Process("3", ProcessPriority.Low))
        tm.add(Process("4", ProcessPriority.Medium))
        tm.add(Process("5", ProcessPriority.High))

        assertIterableEquals(listOf("2", "5", "4"), tm.list(SortBy.Priority).map { x -> x.pid })
    }

    @Test
    fun kill() {
        val tm = TaskManager(3, TaskManagerStoreStrategy.Priority)
        val p1 = Process("1", ProcessPriority.Medium)
        tm.add(p1)
        tm.add(Process("2", ProcessPriority.High))
        tm.add(Process("3", ProcessPriority.Low))
        tm.kill("1")
        assertFalse(p1.running)
        assertEquals(2, tm.list().size)
    }

    @Test
    fun `kill by priority group`() {
        val tm = TaskManager(3, TaskManagerStoreStrategy.FIFO)
        val p1 = Process("1", ProcessPriority.Medium)
        val p2 = Process("2", ProcessPriority.Medium)
        tm.add(p1)
        tm.add(p2)
        tm.add(Process("3", ProcessPriority.Low))
        tm.kill(ProcessPriority.Medium)
        assertFalse(p1.running || p2.running)
        assertEquals(1, tm.list().size)
    }

    @Test
    fun killAll() {

        val tm = TaskManager(3, TaskManagerStoreStrategy.FIFO)
        tm.add(Process("1", ProcessPriority.Medium))
        tm.add(Process("2", ProcessPriority.High))
        tm.add(Process("3", ProcessPriority.Low))
        tm.killAll()
        assertEquals(0, tm.list().size)
    }
}