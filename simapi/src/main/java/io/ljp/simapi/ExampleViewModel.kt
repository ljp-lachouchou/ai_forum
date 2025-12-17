package io.ljp.simapi

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch

class ExampleViewModel : ViewModel() {
    fun fetchData() = viewModelScope.launch {
        apiClient {
            val a = get<Any> {
                apiRequest("welcome.html") {
                    params = mapOf("userId" to "123")
                }
            }
            Log.e("ExampleViewModel", "a: $a")
        }
    }
}