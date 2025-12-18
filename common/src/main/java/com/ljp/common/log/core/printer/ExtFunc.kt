package com.ljp.common.log.core.printer

import android.content.Context
import com.ljp.common.log.core.priority.BaseLogPriority
import com.ljp.common.log.core.priority.LogLevel
val Any?.simName: String
    get() = this?.javaClass?.simpleName ?: "Any is  null"

inline fun <reified  T: LogPrinter> Any?.i(priority: BaseLogPriority<T>,msg:Any?) {
    priority.printer.print(
        priority.getPriority(LogLevel.INFO),
        this.simName,
        msg.toString()
    )
}
inline fun <reified  T: LogPrinter> Any?.e(priority: BaseLogPriority<T>,msg:Any?) {
    priority.printer.print(
        priority.getPriority(LogLevel.ERROR),
        this.simName,
        msg.toString()
    )
}
inline fun <reified  T: LogPrinter> Any?.w(priority: BaseLogPriority<T>,msg:Any?) {
    priority.printer.print(
        priority.getPriority(LogLevel.WARN),
        this.simName,
        msg.toString()
    )
}
inline fun <reified  T: LogPrinter> Any?.a(priority: BaseLogPriority<T>,msg:Any?) {
    priority.printer.print(
        priority.getPriority(LogLevel.ASSERT),
        this.simName,
        msg.toString()
    )
}
inline fun <reified  T: LogPrinter> Any?.d(priority: BaseLogPriority<T>,msg:Any?) {
    priority.printer.print(
        priority.getPriority(LogLevel.DEBUG),
        this.simName,
        msg.toString()
    )
}
inline fun <reified  T: LogPrinter> Any?.v(priority: BaseLogPriority<T>,msg:Any?) {
    priority.printer.print(
        priority.getPriority(LogLevel.VERBOSE),
        this.simName,
        msg.toString()
    )
}