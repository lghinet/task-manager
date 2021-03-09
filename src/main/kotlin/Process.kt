import java.sql.Timestamp

/**
 * process is identified by 2 fields, a unique unmodifiable identifier(PID), and a priority(low, medium, high).
 * The process is immutable, it is generated with a priority and will die with this priority
 * – each process has a kill() method that will destroy it
 */
data class Process(val pid: String, val priority: ProcessPriority) {
    val createdAt = Timestamp(System.currentTimeMillis())
    var running = true
        private set

    fun kill() {
        running = false
    }
}

enum class ProcessPriority {
    High, Medium, Low
}