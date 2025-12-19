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
fun EventHandler.asEvent(dispatcher: CoroutineDispatcher, eventName: String,targetTag:String? = null): RealBusEvent
        = RealBusEvent(dispatcher, eventName, this, targetTag = targetTag)

fun EventHandler.asInteractionEvent(eventName: String,tag:String?)
        = InteractionEvent(eventName = eventName,event = this, targetTag = tag)

fun LifecycleOwner.observeEvent(eventName: String,myTag:String? = null)
    = lifecycleScope.launch {
    repeatOnLifecycle(Lifecycle.State.STARTED) {
        EventBusHub.getFlowByName(eventName).collect { busEvent ->
            if (busEvent.targetTag == null || busEvent.targetTag == myTag) {
                withContext(busEvent.dispatcher) {
                    busEvent.event.invoke()
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
    val targetTag: String?
}
data class RealBusEvent(
    override val dispatcher: CoroutineDispatcher,
    override val eventName: String,
    override val event: EventHandler, override var targetTag: String?
) : BusEvent
class InteractionEvent(
    override val dispatcher: CoroutineDispatcher = Dispatchers.Main,
    override val eventName: String = "ClickEvent", override val event: EventHandler,
    override val targetTag: String?,
) : BusEvent

