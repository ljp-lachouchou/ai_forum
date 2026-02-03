package io.ljp.simapi.di

import javax.inject.Qualifier


@Qualifier
@Retention(AnnotationRetention.RUNTIME)
annotation class HttpEngineFactory(val httpEngineType : HttpEngineType)
enum class HttpEngineType {
    CIO,
    OKHTTP
}