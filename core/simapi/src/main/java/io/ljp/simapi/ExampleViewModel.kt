package io.ljp.simapi

import android.util.Log
import android.util.Log.i

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject
@HiltViewModel
class ExampleViewModel @Inject constructor(
    private val ac: ApiClient
) : ViewModel() {
    fun fetchData() = viewModelScope.launch {
        ac.apiClient {
            get<Any> {
                apiRequest("weixin_43960383/article/details/120103913") {
                    params = mapOf("userId" to "123")
                }
            }
        }
    }
}