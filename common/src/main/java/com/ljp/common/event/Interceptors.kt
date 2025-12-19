package com.ljp.common.event

import com.ljp.common.log.core.printer.w
import com.ljp.common.log.core.priority.LogcatPriorityInstance

class LogEventInterceptor : EventInterceptor {
    override fun intercept(event: BusEvent): BusEvent?
    = event.also {
        w(LogcatPriorityInstance,
            "[Event] intercept event: eventName : ${event.eventName}\n event: ${event.event}\ntargetTag: ${event.targetTag}\ndispatcher: ${event.dispatcher}")
    }
}
val LogEventInterceptorInstance =  LogEventInterceptor()