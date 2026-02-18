package io.ljp.simapi.client

import com.ljp.common.token.TokenProvider
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
import io.ktor.client.plugins.HttpCallValidator
import io.ktor.client.plugins.HttpTimeout
import io.ktor.client.plugins.auth.Auth
import io.ktor.client.plugins.auth.providers.BearerTokens
import io.ktor.client.plugins.auth.providers.bearer
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.client.plugins.defaultRequest
import io.ktor.client.request.header
import io.ktor.serialization.kotlinx.json.json
import io.ljp.simapi.module.HttpClientModule
import kotlinx.serialization.json.Json


class HttpClientFactory(
    private val modules: List<HttpClientModule>,
    private val tokenProvider: TokenProvider
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
        defaultRequest  {
            url(baseUrl)

        }
        install(HttpTimeout) {
            requestTimeoutMillis = 60000L  // 整个请求的最大耗时
            connectTimeoutMillis = 15000L  // 建立连接的最长等待时间
            socketTimeoutMillis = 60000L   // 两个数据包之间的最大间隔1
        }
        install(Auth) {
            bearer {
                loadTokens {
                    val token = tokenProvider.getToken() ?: return@loadTokens null
                    BearerTokens(accessToken = token, refreshToken = "")
                }
            }
        }
        install(HttpCallValidator) {
            validateResponse {response ->
                val statusCode = response.status.value
                if (statusCode == 401 || statusCode == 403) {
                    // 这里是拦截点
                    handleAuthError(statusCode)
                }
            }
        }
        install(ContentNegotiation) {
            json(
                Json {
                    ignoreUnknownKeys = true
                }
            )
        }
        modules.forEach { it.install(this) }
    }

    private suspend fun handleAuthError(statusCode: Int) {
        println("拦截到权限错误: $statusCode，准备重新登录或刷新 Token")
        tokenProvider.clearToken()
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