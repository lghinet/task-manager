package store

import Process
import java.util.*

/**
 * PriorityProcessStore is a PriorityQueue ProcessStore implementation
 * This Store is not Thread-safe
 */
class PriorityProcessStore(private val capacity: Int) : ProcessStore {
    private val _queue = PriorityQueue(capacity,
        compareByDescending<Process> { it.priority }
            .thenBy { it.createdAt })

    /**
     * when the max size is reached, should result into an evaluation: if the new
     * process passed in the add() call has a higher priority compared to any of
     * the existing one, we remove the lowest priority that is the oldest, otherwise we skip its
     */
    override fun add(process: Process): Boolean {
        if (_queue.size == capacity) {
            val first = _queue.peek()
            if (first.priority > process.priority) {
                val proc = _queue.remove()
                proc.kill()
                _queue.add(process)
                return true
            }
            return false
        }

        _queue.add(process)
        return true
    }

    override fun remove(pid: String): Boolean {
        return _queue.removeIf { x -> x.pid == pid }
    }

    override fun get(pid: String): Process? {
        return _queue.firstOrNull { x -> x.pid == pid }
    }

    override fun getAll(): List<Process> {
        return _queue.toList()
    }
}