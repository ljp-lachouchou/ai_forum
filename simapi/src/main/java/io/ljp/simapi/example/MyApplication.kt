package io.ljp.simapi.example

import android.app.Application
import io.ljp.simapi.ApiService
import io.ljp.simapi.apiClient
import io.ljp.simapi.apiRequest
import io.ljp.simapi.module.LoggingModule

class MyApplication : Application(){
    override fun onCreate() {
        super.onCreate()
        ApiService.init(baseUrl = "https://www.csdn.net/", modules = listOf(LoggingModule(enable = true)))
    }
}