package com.ljp.common.event

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

fun LifecycleOwner.observeEvent(eventName: String,myTag:String,isSticky: Boolean = false)
{
    val subscribeTime =  System.currentTimeMillis()
    lifecycleScope.launch {
        repeatOnLifecycle(Lifecycle.State.STARTED) {
            EventBusHub.getFlowByName(eventName).collect { busEvent ->
                val _isTarget = busEvent.expectedReceivers == null || busEvent.expectedReceivers!!.contains(myTag)
                if (_isTarget) {
                    if (!isSticky &&  busEvent.timestamp < subscribeTime) {
                        return@collect
                    }
                    if  (EventBusHub.hasConsumed(busEvent,myTag)) {
                        return@collect
                    }
                    withContext(busEvent.dispatcher) {
                        busEvent.event.invoke()
                    }
                    EventBusHub.acknowledge(busEvent,myTag)
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
data class RealBusEvent(
    override val dispatcher: CoroutineDispatcher,
    override val eventName: String,
    override val event: EventHandler,
    override val timestamp: Long = System.currentTimeMillis(),
    override val expectedReceivers: Set<String>?
) : BusEvent
class InteractionEvent(
    override val dispatcher: CoroutineDispatcher = Dispatchers.Main,
    override val eventName: String = "ClickEvent", override val event: EventHandler,
     override val timestamp: Long = System.currentTimeMillis(),
    override val expectedReceivers: Set<String>?,
) : BusEvent

