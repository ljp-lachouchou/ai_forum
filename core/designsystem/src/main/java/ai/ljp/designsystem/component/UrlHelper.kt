package ai.ljp.designsystem.component

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.heightIn
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.produceState
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import io.ktor.client.HttpClient
import io.ktor.client.request.get
import io.ktor.client.statement.bodyAsText
import io.ktor.client.statement.readRawBytes
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

private val sharedClient = HttpClient()
@Composable
fun DynamicContent(
    url : String,
    modifier: Modifier = Modifier,
    loadingPlaceholder : @Composable () -> Unit = {
        CircularProgressIndicator()
    },
    errorPlaceholder: @Composable (String) -> Unit = {Text("出错: $it")},
    scope : CoroutineScope = rememberCoroutineScope(),
    text : @Composable (String) -> Unit,
) {
    val client = HttpClient()
    val contentState = produceState<Result<String>?>(initialValue = null,url) {
        value = withContext(Dispatchers.IO) {
            runCatching {
                sharedClient.get(url).bodyAsText()
            }
        }
    }
    when(val result = contentState.value) {
        null -> loadingPlaceholder()
        else -> result.fold(
            onSuccess = {data ->
                Box(
                    modifier = modifier
                ) {
                    text(data)
                }

            },
            onFailure = {
                errorPlaceholder(it.message ?: "未知错误")
            }
        )
    }
}