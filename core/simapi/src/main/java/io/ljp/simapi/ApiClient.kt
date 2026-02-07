package io.ljp.simapi

import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.engine.cio.CIO
import io.ktor.client.engine.okhttp.OkHttp
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
import io.ljp.simapi.di.HttpEngineFactory
import io.ljp.simapi.di.HttpEngineType
import io.ljp.simapi.module.HttpClientModule
import javax.inject.Inject


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
internal typealias RW<T> = ResultWrapper<T>
class ApiClient @Inject constructor(
    @HttpEngineFactory(HttpEngineType.OKHTTP) val httpClient: HttpClient
) {
    suspend inline fun <reified T> get(crossinline block: ()->ApiRequest): RW<T>{
        val apiRequest = block()
        val response = safeApiCall {
            httpClient.get(apiRequest.path) {
                apiRequest.contentType?.let { contentType(it) }
                apiRequest.headers?.forEach { (k, v) -> header(k, v) }
                apiRequest.params?.forEach { (k, v) -> parameter(k, v) }
            }.body<ApiResponse<T>>()
        }
        return response
    }
    suspend inline fun <reified T> post(crossinline block: ()->ApiRequest): RW<T> {
        val apiRequest = block()
        val response =  safeApiCall {

            httpClient.post(apiRequest.path) {
                apiRequest.contentType?.let { contentType(it) } ?: contentType(ContentType.Application.Json)
                apiRequest.body?.let { setBody(it) }
                apiRequest.headers?.forEach { (k, v) -> header(k, v) }
            }.body<ApiResponse<T>>()
        }
        return response
    }
    suspend inline fun <reified T> put(crossinline block: ()->ApiRequest): RW<T> {
        val apiRequest = block()
        val response = safeApiCall {

            httpClient.put(apiRequest.path) {
                apiRequest.contentType?.let { contentType(it) } ?: contentType(ContentType.Application.Json)
                apiRequest.body?.let { setBody(it) }
                apiRequest.headers?.forEach { (k, v) -> header(k, v) }
            }.body<ApiResponse<T>>()
        }
        return response
    }
    suspend inline fun <reified T>  patch(crossinline block: ()->ApiRequest): RW<T> {
        val apiRequest = block()
        val response = safeApiCall {

            httpClient.patch(apiRequest.path) {
                apiRequest.contentType?.let { contentType(it) } ?: contentType(ContentType.Application.Json)
                apiRequest.body?.let { setBody(it) }
                apiRequest.headers?.forEach { (k, v) -> header(k, v) }
            }.body<ApiResponse<T>>()
        }
        return response
    }
    suspend inline fun <reified T> delete(crossinline block: ()->ApiRequest): RW<T> {
        val apiRequest = block()
        val response = safeApiCall {
            httpClient.delete(apiRequest.path) {
                apiRequest.contentType?.let { contentType(it) }
                apiRequest.params?.forEach { (k, v) -> parameter(k, v) }
                apiRequest.headers?.forEach { (k, v) -> header(k, v) }
            }.body<ApiResponse<T>>()
        }
        return response
    }
}
fun apiRequest(path: String,block:ApiRequestBuilder.() -> Unit = {}): ApiRequest {
    val builder = ApiRequestBuilder(path)
    builder.apply(block)
    return builder.build()
}

// 调用工具函数：不再负责“设置”，只负责“提供环境”


suspend inline fun <reified T> ApiClient.apiClient(block:suspend ApiClient.() -> T) : T {
    return block(this)
}
