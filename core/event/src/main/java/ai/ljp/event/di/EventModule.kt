package ai.ljp.event.di

import ai.ljp.event.DebounceInterceptor
import ai.ljp.event.EventInterceptor
import ai.ljp.event.LogEventInterceptor
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
interface EventModule {
    @Binds
    @Interceptor(EventInterceptors.Debounce)
    fun bindsDebounceInterceptor(
        debounceInterceptor : DebounceInterceptor
    ) : EventInterceptor

    @Binds
    @Interceptor(EventInterceptors.Logger)
    fun bindsLogEventInterceptor(
        logEventInterceptor : LogEventInterceptor
    ) : EventInterceptor
}