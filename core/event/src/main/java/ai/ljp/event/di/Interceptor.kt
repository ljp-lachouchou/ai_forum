package ai.ljp.event.di

import javax.inject.Qualifier

@Retention(AnnotationRetention.RUNTIME)
@Qualifier
annotation class Interceptor(
    val eventInterceptors : EventInterceptors
)
enum class EventInterceptors {
    Logger,
    Debounce
}