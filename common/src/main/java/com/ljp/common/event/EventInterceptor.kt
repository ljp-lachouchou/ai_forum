package com.ljp.common.event

interface EventInterceptor {
    fun intercept(event: BusEvent): BusEvent?
}
