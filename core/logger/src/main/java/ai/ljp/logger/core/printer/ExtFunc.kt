package ai.ljp.logger.core.printer

import ai.ljp.logger.core.priority.BaseLogPriority
import ai.ljp.logger.core.priority.LogLevel
import ai.ljp.logger.core.priority.LogcatPriority
import ai.ljp.logger.di.AIForumLogger
import ai.ljp.logger.di.LoggerConfig

val Any?.simName: String
    get() = this?.javaClass?.simpleName ?: "Any is  null"

inline fun <reified  T: LogPrinter> Any?.i(priority: BaseLogPriority<T>,tag : String = simName,msg:Any?) {
    priority.printer.print(
        priority.getPriority(LogLevel.INFO),
        tag,
        msg.toString()
    )
}
inline fun <reified  T: LogPrinter> Any?.e(priority: BaseLogPriority<T>,tag : String = simName,msg:Any?) {
    priority.printer.print(
        priority.getPriority(LogLevel.ERROR),
        tag,
        msg.toString()
    )
}
inline fun <reified  T: LogPrinter> Any?.w(priority: BaseLogPriority<T>,tag : String = simName,msg:Any?) {
    priority.printer.print(
        priority.getPriority(LogLevel.WARN),
        tag,
        msg.toString()
    )
}
inline fun <reified  T: LogPrinter> Any?.a(priority: BaseLogPriority<T>,tag : String = simName,msg:Any?) {
    priority.printer.print(
        priority.getPriority(LogLevel.ASSERT),
        tag,
        msg.toString()
    )
}
inline fun <reified  T: LogPrinter> Any?.d(priority: BaseLogPriority<T>,tag : String = simName,msg:Any?) {
    priority.printer.print(
        priority.getPriority(LogLevel.DEBUG),
        tag,
        msg.toString()
    )
}
inline fun <reified  T: LogPrinter> Any?.v(priority: BaseLogPriority<T>,tag : String = simName,msg:Any?) {
    priority.printer.print(
        priority.getPriority(LogLevel.VERBOSE),
        tag,
        msg.toString()
    )
}

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