package ai.ljp.event

import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext



typealias EventHandler = ()-> Unit
fun <T> Array<out T>.toSetOrNull(): Set<T>? {
    return if (this.isNotEmpty()) this.toSet() else null
}
fun EventHandler.asEvent(dispatcher: CoroutineDispatcher, eventName: String,vararg tag:String): RealBusEvent
        = RealBusEvent(dispatcher, eventName, this, expectedReceivers = tag.toSetOrNull())

fun EventHandler.asInteractionEvent(eventName: String,vararg tag:String)
        = InteractionEvent(eventName = eventName,event = this,expectedReceivers = tag.toSetOrNull())

fun LifecycleOwner.observeEvent(eventName: String,eventBusHub: EventBusHub,myTag:String,isSticky: Boolean = false,navHandler:(NavEvent)-> Unit = {}) {
    val subscribeTime =  System.currentTimeMillis()
    lifecycleScope.launch {
        repeatOnLifecycle(Lifecycle.State.STARTED) {
            eventBusHub.getFlowByName(eventName).collect { busEvent ->
                val _isTarget = busEvent.expectedReceivers == null || busEvent.expectedReceivers!!.contains(myTag)
                if (_isTarget) {
                    if (!isSticky &&  busEvent.timestamp < subscribeTime) {
                        return@collect
                    }
                    if  (eventBusHub.hasConsumed(busEvent,myTag)) {
                        return@collect
                    }
                    when(busEvent) {
                        is NavEvent -> {
                            navHandler(busEvent)
                        }
                        else -> {
                            withContext(busEvent.dispatcher) {
                                busEvent.event.invoke()
                            }
                        }
                    }
                    eventBusHub.acknowledge(busEvent,myTag)
                }
            }
        }
    }
}


interface BusEvent {
    // 线程调度分发器
    val dispatcher: CoroutineDispatcher
    val eventName: String
    val event: EventHandler
    val timestamp: Long
    val expectedReceivers: Set<String>?

}
open class RealBusEvent(
    override val dispatcher: CoroutineDispatcher,
    override val eventName: String,
    override val event: EventHandler,
    override val timestamp: Long = System.currentTimeMillis(),
    override val expectedReceivers: Set<String>?
) : BusEvent
class InteractionEvent(
    dispatcher: CoroutineDispatcher = Dispatchers.Main,
    eventName: String = "ClickEvent", event: EventHandler,
     timestamp: Long = System.currentTimeMillis(),
    expectedReceivers: Set<String>?,
) : RealBusEvent(dispatcher = dispatcher,
    eventName = eventName,
    event = event,
    timestamp = timestamp,
    expectedReceivers = expectedReceivers)

/**
 * 导航事件
 */
data class NavEvent(
    // TODO缺少route
    override val expectedReceivers: Set<String>?
) : RealBusEvent(
    eventName = EVENT_NAME,
    timestamp = System.currentTimeMillis(),
    event = {},dispatcher = Dispatchers.Main,
    expectedReceivers = expectedReceivers
) {
    companion object {
        const val EVENT_NAME = "NavEvent"
    }
}



