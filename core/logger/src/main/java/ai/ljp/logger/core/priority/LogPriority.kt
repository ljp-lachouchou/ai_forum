package ai.ljp.logger.core.priority

import ai.ljp.logger.core.printer.LogPrinter

enum class LogLevel {
    VERBOSE, DEBUG, INFO, WARN, ERROR, ASSERT
}
interface LogPriority<P : LogPrinter> {
    val printer: P
    val infoPriority: Int
    val debugPriority: Int
    val errorPriority: Int
    val warnPriority: Int
    val assertPriority: Int
    val verbosePriority: Int
}