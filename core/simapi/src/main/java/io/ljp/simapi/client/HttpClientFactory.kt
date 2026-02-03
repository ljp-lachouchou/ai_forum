package io.ljp.simapi.client

import io.ktor.client.HttpClient
import io.ktor.client.HttpClientConfig
import io.ktor.client.engine.HttpClientEngine
import io.ktor.client.engine.HttpClientEngineConfig
import io.ktor.client.engine.HttpClientEngineFactory
import io.ktor.client.engine.cio.CIO
import io.ktor.client.engine.cio.CIOEngineConfig
import io.ktor.client.engine.okhttp.OkHttp
import io.ktor.client.engine.okhttp.OkHttpConfig
import io.ktor.client.plugins.DefaultRequest
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.client.plugins.defaultRequest
import io.ktor.serialization.kotlinx.json.json
import io.ljp.simapi.module.HttpClientModule


class HttpClientFactory(
    private val modules: List<HttpClientModule>
) {
    private fun _createCIO(baseUrl: String): HttpClient {
        return HttpClient(CIO) {
            commonConfig(baseUrl)
        }
    }
    private fun _createOkHttp(baseUrl: String): HttpClient {
        return HttpClient(OkHttp) {
            commonConfig(baseUrl)
        }
    }

    private fun <T : HttpClientEngineConfig> HttpClientConfig<T>.commonConfig(
        baseUrl: String
    ) {
        install(DefaultRequest) {
            url(baseUrl)
        }
        install(ContentNegotiation) {
            json()
        }
        modules.forEach { it.install(this) }
    }
    fun create(engineFactory: HttpClientEngineFactory<*>, baseUrl: String): HttpClient = when(engineFactory) {
        CIO ->  _CIO_CLIENT(baseUrl)
        OkHttp -> _OKHTTP_CLIENT(baseUrl)
        else -> throw IllegalArgumentException("Unsupported engine config: ${engineFactory.javaClass.name}")
    }
    private val _CIO_CLIENT: (String) -> HttpClient by lazy {
        { baseUrl: String -> _createCIO(baseUrl) }
    }

    private val _OKHTTP_CLIENT: (String) -> HttpClient by lazy {
        { baseUrl: String -> _createOkHttp(baseUrl) }
    }
}