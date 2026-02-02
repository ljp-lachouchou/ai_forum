package com.ljp.common.network.di

import com.ljp.common.network.AIForumDispatchers
import com.ljp.common.network.Dispatcher
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers

@Module
@InstallIn(SingletonComponent::class)
object DispatcherModule {
    @Provides
    @Dispatcher(AIForumDispatchers.IO)
    fun providersIODispatcher() : CoroutineDispatcher = Dispatchers.IO

    @Provides
    @Dispatcher(AIForumDispatchers.Default)
    fun providersDefaultDispatcher() : CoroutineDispatcher = Dispatchers.Default
}