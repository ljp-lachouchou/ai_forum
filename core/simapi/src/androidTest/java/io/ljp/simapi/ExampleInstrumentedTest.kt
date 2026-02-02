package io.ljp.simapi

import androidx.test.platform.app.InstrumentationRegistry
import androidx.test.ext.junit.runners.AndroidJUnit4
import io.ktor.client.HttpClient
import io.ktor.client.engine.cio.CIO
import io.ktor.client.plugins.defaultRequest
import kotlinx.coroutines.runBlocking

import org.junit.Test
import org.junit.runner.RunWith

import org.junit.Assert.*

/**
 * Instrumented test, which will execute on an Android device.
 *
 * See [testing documentation](http://d.android.com/tools/testing).
 */
@RunWith(AndroidJUnit4::class)
class ExampleInstrumentedTest {
    @Test
    fun useAppContext() {
        // Context of the app under test.
        val appContext = InstrumentationRegistry.getInstrumentation().targetContext
        assertEquals("io.ljp.simapi.test", appContext.packageName)
    }
    @Test
    fun a() = runBlocking<Unit> {
        val a = ApiClient(HttpClient(CIO){
            defaultRequest { url("https://ktor.io/docs/") }
        })
        a.get<Unit> {
            apiRequest("welcome.html") {
                params = mapOf("userId" to "123")
            }
        }
    }
}