package com.ljp.common.event

import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.channels.BufferOverflow
import kotlinx.coroutines.flow.MutableSharedFlow
import java.util.concurrent.ConcurrentHashMap
import java.util.concurrent.CopyOnWriteArrayList



object EventBusHub {
    private val interceptors = CopyOnWriteArrayList<EventInterceptor>()
    private val processedRecords = ConcurrentHashMap<Int, MutableSet<String>>()

    fun addInterceptor(interceptor: EventInterceptor) {
        interceptors.add(interceptor)
    }
    init {
        intercept(LogEventInterceptorInstance)
        intercept(DebounceInterceptorInstance)
    }
    private val _eventFlows = ConcurrentHashMap<String, MutableSharedFlow<BusEvent>>()

    fun getFlowByName(name: String): MutableSharedFlow<BusEvent> {
        return _eventFlows.computeIfAbsent(name) {
            MutableSharedFlow(
                replay = 1,
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
        processedRecords[originalEvent.hashCode()] = ConcurrentHashMap.newKeySet()
        currentEvent?.let { event ->
            getFlowByName(event.eventName).tryEmit(event)
        }
    }
    @OptIn(ExperimentalCoroutinesApi::class)
    private fun clearStickyEvent(name: String) {
        val flow = _eventFlows[name] ?:  return
        flow.resetReplayCache()
    }
    fun hasConsumed(event: BusEvent,receiveTag:String) :  Boolean {
        return  processedRecords[event.hashCode()]?.contains(receiveTag) ?: false
    }
    fun acknowledge(event: BusEvent,receiveTag:String) {
        val id = event.hashCode()
        val excepted = event.expectedReceivers ?: return
        val processed = processedRecords[id] ?: return
        processed.add(receiveTag)
        if (processed.containsAll(excepted)) {
            clearStickyEvent(event.eventName)
            processedRecords.remove(id)

        }
    }

}
fun intercept(interceptor: EventInterceptor) {
    EventBusHub.addInterceptor(interceptor)
}
fun BusEvent.publish() {
    EventBusHub.publish(this)
}