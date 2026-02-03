package io.ljp.simapi.di

import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import io.ljp.simapi.module.HttpClientModule
import io.ljp.simapi.module.LoggingModule

@Module
@InstallIn(SingletonComponent::class)
internal object HttpModuleProvider {
    @Provides
    @HttpModule(ModuleType.Logging)
    fun providersLoggingHttpModule() : HttpClientModule = LoggingModule(true)

}