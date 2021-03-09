package store
import Process

interface ProcessStore {
    fun add(process: Process): Boolean
    fun remove(pid: String): Boolean
    fun get(pid: String): Process?
    fun getAll(): List<Process>
}