package ai.ljp.designsystem.component

import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.produceState
import io.ktor.client.HttpClient
import io.ktor.client.request.get
import io.ktor.client.statement.readRawBytes

@Composable
fun DynamicContent(
    url : String,
    loadingPlaceholder : @Composable () -> Unit = {
        CircularProgressIndicator()
    },
    errorPlaceholder: @Composable (String) -> Unit = {Text("出错: $it")},
    text : @Composable (String) -> Unit,
) {
    val client = HttpClient()
    val contentState = produceState<Result<String>?>(initialValue = null,url) {
        value = runCatching { client.get(url).readRawBytes().decodeToString() }
    }
    when(val result = contentState.value) {
        null -> loadingPlaceholder()
        else -> result.fold(
            onSuccess = {data ->
                text(data)
            },
            onFailure = {
                errorPlaceholder(it.message ?: "未知错误")
            }
        )
    }
}