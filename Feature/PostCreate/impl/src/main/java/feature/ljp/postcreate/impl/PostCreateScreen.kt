package feature.ljp.postcreate.impl

import ai.ljp.designsystem.component.markdown.render.tool.MarkdownStyle
import ai.ljp.ui.AIForumToolbar
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContract
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeDrawing
import androidx.compose.foundation.layout.windowInsetsBottomHeight
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.ProvideTextStyle
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.unit.sp
import com.ljp.model.WordTag
import feature.ljp.postcreate.api.R
import java.io.InputStream


@Composable
private fun PostCreateScreen(
    postCreateUiState: PostCreateUiState,
    textValue : TextFieldValue,
    title : String,
    onBackClick : () -> Unit,
    onTitleChanged : (String) -> Unit,
    onCreatePost : (InputStream?, String, List<WordTag>, String) -> Unit,
    applyMarkdownStyle : (MarkdownStyle) -> Unit,
    onContentChange : (TextFieldValue) -> Unit,
    modifier: Modifier = Modifier
) {
    Scaffold(
        modifier = modifier,
        topBar = {
            AIForumToolbar(
                modifier = Modifier.fillMaxWidth(),
                titleRes = R.string.feature_post_create_api_title,
                onBackClick = onBackClick,
                actionIcon = {
                    OutlinedButton(onClick = {
                        onCreatePost(
                            textValue.text.byteInputStream(),
                            "",//TODO
                            emptyList(),
                            title
                        )
                    }) {
                        ProvideTextStyle(MaterialTheme.typography.headlineMedium) {
                            Text(text = stringResource(R.string.feature_post_create_api_create))
                        }
                    }
                }
            )
        }
    ) {innerPadding->
        Column(
            modifier = Modifier.fillMaxSize().padding(innerPadding),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            TitleTextField(
                textValue = textValue,
                onContentChange = onContentChange
            )
            Spacer(Modifier.windowInsetsBottomHeight(WindowInsets.safeDrawing))
        }
    }
}
@Composable
private fun TitleTextField(
    textValue : TextFieldValue,
    onContentChange : (TextFieldValue) -> Unit,
) {
    val focusRequester = remember { FocusRequester() }
    TextField(
        value = textValue,
        onValueChange = onContentChange,
        textStyle = TextStyle(
            color = Color.Black,
            fontSize = 20.sp,           // 决定了文字和光标的大小
            fontWeight = FontWeight.Bold, // 加粗
            letterSpacing = 2.sp,        // 字间距
            lineHeight = 30.sp           // 行高
        ),
        modifier = Modifier
            .focusRequester(focusRequester)
    )
}