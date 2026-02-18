package ai.ljp.designsystem

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import io.ktor.client.HttpClient
import io.ktor.client.request.get
import io.ktor.client.statement.bodyAsText
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import javax.inject.Inject

@Composable
fun rememberLoadUrlState(
    url: String? = null,
    scope: CoroutineScope = rememberCoroutineScope()
) : UrlState {
    val loadState = remember { UrlState(scope) }
    LaunchedEffect(url) {
        url?.let { loadState.load(it) }
    }
    return loadState
}
sealed interface UrlLoadingState {
    data object Idle : UrlLoadingState
    data class  Error(val exception: Throwable) : UrlLoadingState
    data object Loading : UrlLoadingState
    data class Success(val content : String) : UrlLoadingState
}
class UrlState(
    private val scope : CoroutineScope,

) {
    var state by mutableStateOf<UrlLoadingState>(UrlLoadingState.Idle)
        private set
    private val client : HttpClient = HttpClient()
    fun load(url: String?) {
        if (url.isNullOrBlank()) return
        scope.launch {
            state = UrlLoadingState.Loading
            state = try {
                val response: String = client.get(url).bodyAsText()
                UrlLoadingState.Success(response)
            } catch (e: Exception) {
                UrlLoadingState.Error(e)
            }
        }
    }

}