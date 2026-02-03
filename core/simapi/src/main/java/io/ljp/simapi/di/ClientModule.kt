package io.ljp.simapi.di

import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import io.ktor.client.HttpClient
import io.ktor.client.engine.HttpClientEngineFactory
import io.ktor.client.engine.cio.CIO
import io.ktor.client.engine.okhttp.OkHttp
import io.ljp.simapi.ApiClient
import io.ljp.simapi.BuildConfig
import io.ljp.simapi.client.HttpClientFactory
import io.ljp.simapi.module.HttpClientModule
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
internal object ClientModule {
    @Provides
    @HttpEngineFactory(HttpEngineType.CIO)
    fun providersCioEngineFactory() : HttpClientEngineFactory<*> = CIO

    @Provides
    @HttpEngineFactory(HttpEngineType.OKHTTP)
    fun providersOkhttpEngineFactory() : HttpClientEngineFactory<*> = OkHttp

    @Provides
    @Singleton
    fun providersClientFactory(
        @HttpModule(ModuleType.Logging) loggingModule : HttpClientModule
    ) : HttpClientFactory =
        HttpClientFactory(listOf(loggingModule))

    @Provides
    @Singleton
    @HttpEngineFactory(HttpEngineType.OKHTTP)
    fun providersOkhttpClient(
        @HttpEngineFactory(HttpEngineType.OKHTTP) okHttpEngine : HttpClientEngineFactory<*>,
        clientFactory : HttpClientFactory
    ) : HttpClient = clientFactory.create(okHttpEngine, BuildConfig.BACKEND_URL )



}