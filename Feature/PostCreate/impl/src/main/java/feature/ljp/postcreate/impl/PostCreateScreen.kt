package feature.ljp.postcreate.impl

import ai.ljp.designsystem.component.markdown.render.tool.FullMarkdownEditor
import ai.ljp.designsystem.component.markdown.render.tool.MarkdownStyle
import ai.ljp.designsystem.component.markdown.render.tool.MarkdownToolbar
import ai.ljp.ui.AIForumToolbar
import ai.ljp.ui.Category
import ai.ljp.ui.CategoryFlowRow
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContract
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.imePadding
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
    category: Category,
    onBackClick : () -> Unit,
    onTitleChanged : (String) -> Unit,
    onCreatePost : (InputStream?, String, List<WordTag>, String) -> Unit,
    applyMarkdownStyle : (MarkdownStyle) -> Unit,
    onContentChange : (TextFieldValue) -> Unit,
    onCategoryChanged : (Category)-> Unit,
    modifier: Modifier = Modifier
) {
    val focusRequester = remember { FocusRequester() }
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
                title = title,
                onTitleChanged = onTitleChanged,
                focusRequester = focusRequester
            )
            CategoryFlowRow(
                currentCategory = category,
                onCategoryChanged = onCategoryChanged,
                modifier = Modifier.fillMaxWidth()
            )
            Spacer(Modifier.windowInsetsBottomHeight(WindowInsets.safeDrawing))
        }
    }
}
@Composable
private fun TitleTextField(
    title : String,
    focusRequester : FocusRequester,
    onTitleChanged : (String) -> Unit,
) {

    TextField(
        value = title,
        onValueChange = onTitleChanged,
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

