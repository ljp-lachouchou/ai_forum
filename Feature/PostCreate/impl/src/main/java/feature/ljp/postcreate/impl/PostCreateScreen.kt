package feature.ljp.postcreate.impl

import ai.ljp.designsystem.component.markdown.render.tool.MarkdownStyle
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.TextFieldValue
import com.ljp.model.WordTag


@Composable
private fun PostCreateScreen(
    postCreateUiState: PostCreateUiState,
    textValue : TextFieldValue,
    onBackClick : () -> Unit,
    onCreatePost : (String, ByteArray, String, List<WordTag>, String) -> Unit,
    applyMarkdownStyle : (MarkdownStyle) -> Unit,
    modifier: Modifier = Modifier
) {

}