import store.DefaultProcessStore
import store.FIFOProcessStore
import store.PriorityProcessStore
import store.ProcessStore

/**
 * Task Manager is a software component that is designed for handling
 * multiple processes inside an operating system
 */
class TaskManager(capacity: Int, storeStrategy: TaskManagerStoreStrategy) {
    private val _store = createStore(capacity, storeStrategy)

    /**
     * The task manager should have a prefixed maximum capacity, so it can not have more than a certain
     * number of running processes within itself.This value is defined at build time.
     * The add(process) method in TM is used for it.
     */
    fun add(process: Process): Boolean {
        return _store.add(process)
    }

    private fun createStore(capacity: Int, storeStrategy: TaskManagerStoreStrategy): ProcessStore {
        return when (storeStrategy) {
            TaskManagerStoreStrategy.Priority -> PriorityProcessStore(capacity)
            TaskManagerStoreStrategy.FIFO -> FIFOProcessStore(capacity)
            else -> DefaultProcessStore(capacity)
        }
    }

    /**
     * The task manager offers the possibility to list() all the running processes,
     * sorting them by time of creation (implicitly we can consider it the time in which
     * has been added to the TM), priority or id.
     */
    fun list(sortBy: SortBy = SortBy.PID): List<Process> {
        val query = _store.getAll().filter { x -> x.running }

        return when (sortBy) {
            SortBy.PID -> query.sortedBy { x -> x.pid }
            SortBy.Priority -> query.sortedBy { x -> x.priority }
            else -> query.sortedBy { x -> x.createdAt }
        }
    }

    /**
     * killing a specific process by PID
     */
    fun kill(pid: String) {
        val process = _store.get(pid)
        if (process != null) {
            process.kill()
            _store.remove(process.pid)
        }
    }

    /**
     *  killing all processes with a specific priority
     */
    fun kill(priority: ProcessPriority) {
        _store
            .getAll()
            .filter { x -> x.priority == priority && x.running }
            .forEach {
                it.kill()
                _store.remove(it.pid)
            }
    }

    /**
     * killing all running processes
     */
    fun killAll() {
        _store
            .getAll()
            .filter { x -> x.running }
            .forEach {
                it.kill()
                _store.remove(it.pid)
            }
    }
}

enum class TaskManagerStoreStrategy {
    Default, FIFO, Priority
}

enum class SortBy {
    CreationDate, PID, Priority
}