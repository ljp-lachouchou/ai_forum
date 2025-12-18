package com.ljp.common.log.core.priority

import com.ljp.common.log.core.printer.LogPrinter


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