package io.ljp.simapi

import com.ljp.common.log.core.printer.w
import com.ljp.common.log.core.priority.LogcatPriorityInstance
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.engine.cio.CIO
import io.ktor.client.engine.okhttp.OkHttp
import io.ktor.client.plugins.defaultRequest
import io.ktor.client.request.delete
import io.ktor.client.request.get
import io.ktor.client.request.header
import io.ktor.client.request.parameter
import io.ktor.client.request.patch
import io.ktor.client.request.post
import io.ktor.client.request.put
import io.ktor.client.request.setBody
import io.ktor.http.ContentType
import io.ktor.http.contentType
import io.ljp.simapi.client.HttpClientFactory
import io.ljp.simapi.client.HttpEngineType
import io.ljp.simapi.module.HttpClientModule
import kotlinx.coroutines.runBlocking

class ApiRequestBuilder(var path: String){

    var params: Map<String, Any>? = null
    var headers: Map<String, String>? = null
    var contentType: ContentType? = null
    var body: Any? = null
    fun  build(): ApiRequest {
        return ApiRequest(path, params, headers, contentType, body)
    }
}

data class ApiRequest(
    val path: String,
    val params: Map<String, Any>?,
    val headers: Map<String, String>?,
    val contentType: ContentType?,
    val body: Any?
)
typealias RW<T> = ResultWrapper<T>
class ApiClient(val httpClient: HttpClient) {
    suspend inline fun <reified T> get( block: ()->ApiRequest): RW<T>{
        val apiRequest = block()
        val response = safeApiCall {
            httpClient.get(apiRequest.path) {
                apiRequest.contentType?.let { contentType(it) }
                apiRequest.headers?.forEach { (k, v) -> header(k, v) }
                apiRequest.params?.forEach { (k, v) -> parameter(k, v) }
            }.body<ApiResponse<T>>()
        }
        w(LogcatPriorityInstance,response)
        return response
    }
    suspend inline fun <reified T> post( block: ()->ApiRequest): RW<T> {
        val apiRequest = block()
        val response =  safeApiCall {

            httpClient.post(apiRequest.path) {
                apiRequest.contentType?.let { contentType(it) } ?: contentType(ContentType.Application.Json)
                apiRequest.body?.let { setBody(it) }
                apiRequest.headers?.forEach { (k, v) -> header(k, v) }
            }.body<ApiResponse<T>>()
        }
        w(LogcatPriorityInstance,response)
        return response
    }
    suspend inline fun <reified T> put( block: ()->ApiRequest): RW<T> {
        val apiRequest = block()
        val response = safeApiCall {

            httpClient.put(apiRequest.path) {
                apiRequest.contentType?.let { contentType(it) } ?: contentType(ContentType.Application.Json)
                apiRequest.body?.let { setBody(it) }
                apiRequest.headers?.forEach { (k, v) -> header(k, v) }
            }.body<ApiResponse<T>>()
        }
        w(LogcatPriorityInstance,response)
        return response
    }
    suspend inline fun <reified T>  patch( block: ()->ApiRequest): RW<T> {
        val apiRequest = block()
        val response = safeApiCall {

            httpClient.patch(apiRequest.path) {
                apiRequest.contentType?.let { contentType(it) } ?: contentType(ContentType.Application.Json)
                apiRequest.body?.let { setBody(it) }
                apiRequest.headers?.forEach { (k, v) -> header(k, v) }
            }.body<ApiResponse<T>>()
        }
        w(LogcatPriorityInstance,response)
        return response
    }
    suspend inline fun <reified T> delete( block: ()->ApiRequest): RW<T> {
        val apiRequest = block()
        val response = safeApiCall {
            httpClient.delete(apiRequest.path) {
                apiRequest.contentType?.let { contentType(it) }
                apiRequest.params?.forEach { (k, v) -> parameter(k, v) }
                apiRequest.headers?.forEach { (k, v) -> header(k, v) }
            }.body<ApiResponse<T>>()
        }
        w(LogcatPriorityInstance,response)
        return response
    }
}
fun apiRequest(path: String,block:ApiRequestBuilder.() -> Unit): ApiRequest {
    val builder = ApiRequestBuilder(path)
    builder.apply(block)
    return builder.build()
}
object ApiService {
    private var _client: HttpClient? = null
    private var _apiClient: ApiClient? = null

    fun init(
        engineType: HttpEngineType = HttpEngineType.CIO,
        baseUrl: String,
        modules: List<HttpClientModule> = listOf()
    ) {
        if (_client != null) return

        val engineFactory = when(engineType) {
            HttpEngineType.CIO -> CIO
            HttpEngineType.OKHTTP -> OkHttp
        }
        val factory = HttpClientFactory(modules)
        _client = factory.create(engineFactory, baseUrl)
        _apiClient = ApiClient(_client!!)
    }

    val apiClient: ApiClient
        get() = _apiClient ?: throw IllegalStateException("必须先调用 ApiService.init()!")
}

// 调用工具函数：不再负责“设置”，只负责“提供环境”
suspend fun apiClient(block:suspend ApiClient.() -> Unit) {
    block(ApiService.apiClient)
}
