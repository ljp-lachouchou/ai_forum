package io.ljp.simapi

import android.util.Log
import android.util.Log.i

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch
class ExampleViewModel : ViewModel() {
    fun fetchData() = viewModelScope.launch {
        apiClient {
            get<Any> {
                apiRequest("weixin_43960383/article/details/120103913") {
                    params = mapOf("userId" to "123")
                }
            }
        }
    }
}