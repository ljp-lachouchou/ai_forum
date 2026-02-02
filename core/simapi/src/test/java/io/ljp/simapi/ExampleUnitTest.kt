package io.ljp.simapi

import io.ktor.client.HttpClient
import io.ktor.client.engine.cio.CIO
import io.ktor.client.plugins.defaultRequest
import kotlinx.coroutines.runBlocking
import org.junit.Test

import org.junit.Assert.*

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * See [testing documentation](http://d.android.com/tools/testing).
 */
class ExampleUnitTest {
    @Test
    fun addition_isCorrect() {
        assertEquals(4, 2 + 2)
    }
    @Test
    fun a() = runBlocking<Unit> {
        val a = ApiClient(HttpClient(CIO){
            defaultRequest { url("https://ktor.io/docs/") }
        })
        a.get<ApiRequest> {
            apiRequest("welcome.html") {
                params = mapOf("userId" to "123")
            }
        }
    }
}