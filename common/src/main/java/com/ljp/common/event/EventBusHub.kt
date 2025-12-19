package com.ljp.common.event

import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.channels.BufferOverflow
import kotlinx.coroutines.flow.MutableSharedFlow
import java.util.concurrent.ConcurrentHashMap
import java.util.concurrent.CopyOnWriteArrayList



object EventBusHub {
    private val interceptors = CopyOnWriteArrayList<EventInterceptor>()

    fun addInterceptor(interceptor: EventInterceptor) {
        interceptors.add(interceptor)
    }
    init {
        intercept(LogEventInterceptorInstance)
    }
    private val _eventFlows = ConcurrentHashMap<String, MutableSharedFlow<BusEvent>>()

    fun getFlowByName(name: String): MutableSharedFlow<BusEvent> {
        return _eventFlows.computeIfAbsent(name) {
            MutableSharedFlow(
                replay = 0,
                extraBufferCapacity = 64,
                onBufferOverflow = BufferOverflow.DROP_OLDEST
            )
        }
    }

    fun publish(originalEvent: BusEvent) {
        var currentEvent: BusEvent? = originalEvent

        for (interceptor in interceptors) {
            currentEvent = currentEvent?.let { interceptor.intercept(it) }
            if (currentEvent == null) return
        }

        currentEvent?.let { event ->
            getFlowByName(event.eventName).tryEmit(event)
        }
    }
}
fun intercept(interceptor: EventInterceptor) {
    EventBusHub.addInterceptor(interceptor)
}
fun BusEvent.publish() {
    EventBusHub.publish(this)
}