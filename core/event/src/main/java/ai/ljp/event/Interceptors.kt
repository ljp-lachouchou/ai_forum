package ai.ljp.event


import ai.ljp.logger.core.printer.LogcatPrinter
import ai.ljp.logger.core.printer.w
import ai.ljp.logger.core.priority.BaseLogPriority
import ai.ljp.logger.core.priority.LogcatPriority
import ai.ljp.logger.di.AIForumLogger
import ai.ljp.logger.di.LoggerConfig
import java.util.concurrent.ConcurrentHashMap
import javax.inject.Inject

class LogEventInterceptor @Inject constructor(
     @LoggerConfig(AIForumLogger.Logcat)
    private val loggerPriority : BaseLogPriority<LogcatPrinter>
) : EventInterceptor {

    override fun intercept(event: BusEvent): BusEvent?
    = event.also {
        w(loggerPriority,
            "[Event] intercept event: eventName : ${event.eventName}\n event: ${event.event}\ntargetTag: ${event.expectedReceivers}\ndispatcher: ${event.dispatcher}")
    }
}
class DebounceInterceptor @Inject constructor(
    @LoggerConfig(AIForumLogger.Logcat)
    private val loggerPriority : BaseLogPriority<LogcatPrinter>,
    private val thresholdMs: Long = 500L) : EventInterceptor {

    private val lastTriggerMap = ConcurrentHashMap<String, Long>()

    override fun intercept(event: BusEvent): BusEvent? {
        val currentTime = System.currentTimeMillis()
        val lastTime = lastTriggerMap[event.eventName] ?: 0L

        // 计算间隔
        return if (currentTime - lastTime < thresholdMs) {
            w(loggerPriority, "[Debounce] 拦截重复点击: ${event.eventName}")
            null
        } else {
            lastTriggerMap[event.eventName] = currentTime
            event
        }
    }
}