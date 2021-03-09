package store

import Process

/**
 * DefaultProcessStore is the default ProcessStore implementation
 * This Store is not Thread-safe
 */
class DefaultProcessStore(private val capacity: Int) : ProcessStore {
    private val _processes = mutableListOf<Process>()

    /**
     * The default behaviour is that we can accept new processes till when there is capacity
     * inside the Task Manager, otherwise we won’t accept any new process
     */
    override fun add(process: Process): Boolean {
        if (_processes.size >= capacity)
            return false

        _processes.add(process)
        return true
    }

    override fun remove(pid: String): Boolean {
        return _processes.removeIf { x -> x.pid == pid }
    }

    override fun get(pid: String): Process? {
        return _processes.firstOrNull { x -> x.pid == pid }
    }

    override fun getAll(): List<Process> {
        return _processes.toList()
    }
}