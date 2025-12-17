package io.ljp.simapi.module

import android.util.Log
import io.ktor.client.HttpClientConfig
import io.ktor.client.plugins.logging.ANDROID
import io.ktor.client.plugins.logging.LogLevel
import io.ktor.client.plugins.logging.Logger
import io.ktor.client.plugins.logging.Logging
import io.ktor.http.HttpHeaders

class LoggingModule(override val enable: Boolean) : HttpClientModule {
    override fun install(config: HttpClientConfig<*>) {
        if (!enable) return
        config.install(Logging) {
            logger = Logger.ANDROID
            level = LogLevel.ALL
            sanitizeHeader { header -> header == HttpHeaders.Authorization }
        }
    }
}