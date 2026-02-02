package io.ljp.simapi.module

import io.ktor.client.HttpClientConfig

interface HttpClientModule {
    val enable: Boolean
    fun install(config: HttpClientConfig<*>)
}