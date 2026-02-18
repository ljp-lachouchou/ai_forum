package feature.ljp.postcreate.impl

import ai.ljp.designsystem.component.AIForumLoadingWheel
import ai.ljp.designsystem.component.markdown.render.tool.FullMarkdownEditor
import ai.ljp.designsystem.component.markdown.render.tool.rememberMarkdownEditorManager
import ai.ljp.ui.AIForumToolbar
import ai.ljp.ui.Category
import ai.ljp.ui.CategoryFlowRow
import ai.ljp.ui.PickOnly
import ai.ljp.ui.rememberLauncherImageForActivityResult
import android.content.Context
import android.net.Uri
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.consumeWindowInsets
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
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.ljp.model.WordTag
import feature.ljp.postcreate.api.R
import java.io.InputStream

@Composable
internal fun PostCreateScreen(
    modifier: Modifier = Modifier,
    onBackClick : () -> Unit,
    viewModel: PostCreateViewModel = hiltViewModel()
) {
    val postCreateUiState by viewModel.postCreateUiState.collectAsStateWithLifecycle()
    val title by viewModel.title.collectAsStateWithLifecycle()
    val curImageUrl by viewModel.curImageUrl.collectAsStateWithLifecycle()
    val category by viewModel.category.collectAsStateWithLifecycle()
    PostCreateScreen(
        postCreateUiState = postCreateUiState,
        title = title,
        curImageUrl = curImageUrl,
        categoryOrdinal = category,
        onBackClick = onBackClick,
        onTitleChanged = viewModel::onTitleChanged,
        onCreatePost = viewModel::onCreatePost,
        onCategoryOrdinalChanged = viewModel::onCategoryOrdinalChanged,
        onUploadImage = viewModel::onUploadImage,
        modifier = modifier
    )
}
@Composable
private fun PostCreateScreen(
    postCreateUiState: PostCreateUiState,
    title : String,
    curImageUrl : String,
    categoryOrdinal: Int,
    onBackClick : () -> Unit,
    onTitleChanged : (String) -> Unit,
    onCreatePost : (InputStream?, String, List<WordTag>, String) -> Unit,
    onCategoryOrdinalChanged : (Category)-> Unit,
    onUploadImage : (Context, Uri) -> Unit,
    modifier: Modifier = Modifier
) {
    val focusRequester = remember { FocusRequester() }
    val manager = rememberMarkdownEditorManager()
    val context = LocalContext.current
    val category = Category.fromOrdinal(categoryOrdinal) ?: Category.Tech
    val picLauncher = rememberLauncherImageForActivityResult {uri ->
         onUploadImage(context,uri)
         manager.apply {
            insertImage(
                currentIndex = focusedIndex,
                url = curImageUrl
            )
        }
    }
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
                            manager.exportMarkdown().byteInputStream(),
                            category.name,
                            emptyList(),
                            title
                        )
                    }) {
                        if (postCreateUiState is PostCreateUiState.Loading) {
                            AIForumLoadingWheel(Modifier.align(Alignment.CenterVertically))
                        }else {
                            ProvideTextStyle(MaterialTheme.typography.headlineMedium) {
                                Text(text = stringResource(R.string.feature_post_create_api_create))
                            }
                        }
                    }
                }
            )
        }
    ) {innerPadding->
        Column(
            modifier = Modifier.fillMaxSize().padding(innerPadding)
                .consumeWindowInsets(innerPadding),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            TitleTextField(
                title = title,
                onTitleChanged = onTitleChanged,
                focusRequester = focusRequester
            )
            CategoryFlowRow(
                currentCategory = category,
                onCategoryChanged = onCategoryOrdinalChanged,
                modifier = Modifier.fillMaxWidth()
            )
            FullMarkdownEditor(
                manager = manager
            ) {
                picLauncher.launch(
                    PickOnly
                )

            }
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

