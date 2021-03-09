package store

import Process

/**
 * FIFOProcessStore is a FIFO ProcessStore implementation
 * This Store is not Thread-safe
 */
class FIFOProcessStore(private val capacity: Int) : ProcessStore {
    private val _queue = ArrayDeque<Process>()

    /**
     * accept all new processes through the add() method, killing and removing from the TM list
     * the oldest one(First-In, First-Out) when the max size is reached
     */
    override fun add(process: Process): Boolean {
        if (_queue.size == capacity) {
            val proc = _queue.removeFirst()
            proc.kill()
        }
        _queue.addLast(process)
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
