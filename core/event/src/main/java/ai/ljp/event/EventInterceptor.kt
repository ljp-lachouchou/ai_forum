package ai.ljp.event

interface EventInterceptor {
    fun intercept(event: BusEvent): BusEvent?
}
