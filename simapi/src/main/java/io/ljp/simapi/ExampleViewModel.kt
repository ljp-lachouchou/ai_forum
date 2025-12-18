package io.ljp.simapi

import android.util.Log

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ljp.common.log.core.printer.e
import com.ljp.common.log.core.priority.LogcatPriorityInstance
import kotlinx.coroutines.launch
class ExampleViewModel : ViewModel() {
    fun fetchData() = viewModelScope.launch {
        apiClient {
            val a = get<Any> {
                apiRequest("weixin_43960383/article/details/120103913") {
                    params = mapOf("userId" to "123")
                }
            }.let { e(LogcatPriorityInstance,it) }



        }
    }
}