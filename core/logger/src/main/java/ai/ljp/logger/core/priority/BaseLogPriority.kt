package ai.ljp.logger.core.priority

import ai.ljp.logger.core.printer.LogPrinter


abstract class BaseLogPriority<T: LogPrinter> : LogPriority<T> {
    fun getPriority(level: LogLevel): Int = when (level) {
        LogLevel.VERBOSE -> verbosePriority
        LogLevel.DEBUG -> debugPriority
        LogLevel.INFO -> infoPriority
        LogLevel.WARN -> warnPriority
        LogLevel.ERROR -> errorPriority
        LogLevel.ASSERT -> assertPriority
    }
}