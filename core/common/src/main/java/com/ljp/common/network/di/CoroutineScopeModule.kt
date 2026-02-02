package com.ljp.common.network.di

import com.ljp.common.network.AIForumDispatchers
import com.ljp.common.network.Dispatcher
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.SupervisorJob
import javax.inject.Qualifier
import javax.inject.Singleton

@Retention(AnnotationRetention.RUNTIME)
@Qualifier
annotation class ApplicationScope

@Module
@InstallIn(SingletonComponent::class)
object CoroutineScopeModule {
    @Provides
    @Singleton
    @ApplicationScope
    fun providersCoroutineScope(
        @Dispatcher(AIForumDispatchers.Default) dispatcher: CoroutineDispatcher,
    ) : CoroutineScope = CoroutineScope(SupervisorJob() + dispatcher)
}