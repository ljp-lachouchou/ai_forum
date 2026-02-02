package ai.ljp.event


import ai.ljp.event.di.EventInterceptors
import ai.ljp.event.di.Interceptor
import ai.ljp.logger.core.printer.LogcatPrinter
import ai.ljp.logger.core.printer.w
import ai.ljp.logger.core.priority.BaseLogPriority
import ai.ljp.logger.core.priority.LogcatPriority
import ai.ljp.logger.di.AIForumLogger
import ai.ljp.logger.di.LoggerConfig
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.Job
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.channels.BufferOverflow
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.isActive
import kotlinx.coroutines.launch
import java.util.concurrent.ConcurrentHashMap
import java.util.concurrent.CopyOnWriteArrayList
import javax.inject.Inject


class EventBusHub @Inject constructor(
    @Interceptor(EventInterceptors.Logger) private val
    loggInterceptor : EventInterceptor,
    @Interceptor(EventInterceptors.Debounce)
    private val debounceInterceptor: EventInterceptor,
    @LoggerConfig(AIForumLogger.Logcat)
    private val logcatPriority: BaseLogPriority<LogcatPrinter>
) {
    companion object {
        const val DEFAULT_BUFFER_EXIST_TIME = 60  * 1000L
        const val DEFAULT_WHEEL_TIME = 5 * 1000L
    }
    private val scope = CoroutineScope(Dispatchers.Default + SupervisorJob())
    private var timeoutJob: Job? = null
    private val interceptors = CopyOnWriteArrayList<EventInterceptor>()
    private val processedRecords = ConcurrentHashMap<Int, MutableSet<String>>()
    private val hasPublishedEvent = ConcurrentHashMap<Int, BusEvent>()

    internal fun addInterceptor(interceptor: EventInterceptor) {
        interceptors.add(interceptor)
    }
    init {
        startUp(listOf(loggInterceptor,debounceInterceptor))
    }


    private fun initTimeoutJob(){
        if  (timeoutJob != null) {
            timeoutJob?.cancel()
            timeoutJob = null
        }
        if (scope.isActive && timeoutJob == null) {
            timeoutJob = scope.launch {
                while (isActive) {
                    delay(DEFAULT_WHEEL_TIME)
                    clearTimeoutEvents()
                }
            }
        }
    }

    private fun clearTimeoutEvents() {
        val now = System.currentTimeMillis()
        val iterator = hasPublishedEvent.values.iterator()
        while (iterator.hasNext()) {
            val event = iterator.next()
            if (now - event.timestamp > DEFAULT_BUFFER_EXIST_TIME) {
                iterator.remove()
                forceCleanup(event.hashCode(),event.eventName)
                w(logcatPriority,"[Event] 清除超时事件: ${event.eventName}")
            }
        }
    }

    fun startUp(interceptors:List<EventInterceptor>) {
        interceptors.forEach { addInterceptor(it) }
        initTimeoutJob()
    }
    private val _eventFlows = ConcurrentHashMap<String, MutableSharedFlow<BusEvent>>()
    private fun forceCleanup(id: Int, eventName: String) {
        // 1. 擦除 SharedFlow 缓存（后来者不再能收到此粘性事件）
        clearStickyEvent(eventName)
        // 2. 移除签收记录
        processedRecords.remove(id)
        // 3. 移除发布的event记录
        hasPublishedEvent.remove(id)
    }

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
        currentEvent?.let { event ->
            val flow = getFlowByName(event.eventName)
            val isPublish = flow.tryEmit(event)
            if(isPublish) {
                processedRecords[currentEvent.hashCode()] = ConcurrentHashMap.newKeySet()
                hasPublishedEvent[currentEvent.hashCode()] =  currentEvent
            }
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
            forceCleanup(id,event.eventName)
        }
    }

}

fun BusEvent.publish(eventBusHub : EventBusHub) {
    eventBusHub.publish(this)
}