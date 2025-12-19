package com.ljp.common.event

import com.ljp.common.log.core.printer.w
import com.ljp.common.log.core.priority.LogcatPriorityInstance
import java.util.concurrent.ConcurrentHashMap

class LogEventInterceptor : EventInterceptor {
    override fun intercept(event: BusEvent): BusEvent?
    = event.also {
        w(LogcatPriorityInstance,
            "[Event] intercept event: eventName : ${event.eventName}\n event: ${event.event}\ntargetTag: ${event.expectedReceivers}\ndispatcher: ${event.dispatcher}")
    }
}
class DebounceInterceptor(private val thresholdMs: Long = 500L) : EventInterceptor {

    private val lastTriggerMap = ConcurrentHashMap<String, Long>()

    override fun intercept(event: BusEvent): BusEvent? {
        val currentTime = System.currentTimeMillis()
        val lastTime = lastTriggerMap[event.eventName] ?: 0L

        // 计算间隔
        return if (currentTime - lastTime < thresholdMs) {
            w(LogcatPriorityInstance, "[Debounce] 拦截重复点击: ${event.eventName}")
            null
        } else {
            lastTriggerMap[event.eventName] = currentTime
            event
        }
    }
}
val LogEventInterceptorInstance =  LogEventInterceptor()
val DebounceInterceptorInstance = DebounceInterceptor()