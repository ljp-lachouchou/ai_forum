package feature.ljp.postcreate.impl

import ai.ljp.data.repository.UserDataRepository
import ai.ljp.data.repository.WordRepository
import ai.ljp.designsystem.component.markdown.render.tool.MarkdownStyle
import ai.ljp.domain.UploadWordDomain
import ai.ljp.ui.Category
import androidx.compose.ui.text.input.TextFieldValue
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ljp.model.WordTag
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import java.io.InputStream
import java.lang.Exception
import javax.inject.Inject
import kotlin.time.Clock
import kotlin.time.ExperimentalTime

@HiltViewModel
class PostCreateViewModel @Inject constructor(
    private val wordRepository: WordRepository,
    private val uploadWordDomain: UploadWordDomain,
    private val savedStateHandle: SavedStateHandle
) : ViewModel() {

    private val _textValue = MutableStateFlow(
        TextFieldValue("")
    )
    val textValue: StateFlow<TextFieldValue> = _textValue.asStateFlow()
    val title : StateFlow<String> =
        savedStateHandle.getStateFlow(TITLE_KEY,"")
    val postCreateUiState : StateFlow<PostCreateUiState> =
        savedStateHandle.getStateFlow(POST_CREATE_STATE_KEY,
            PostCreateUiState.Empty)
    val category : StateFlow<Category> =
        savedStateHandle.getStateFlow(CATEGORY_KEY, Category.Tech)
    // 2. 当用户输入或点击工具栏时，调用此方法
    fun onContentChange(newVal: TextFieldValue) {
        _textValue.value = newVal
    }
    fun onCategoryChanged(category: Category) {
        savedStateHandle[CATEGORY_KEY] = category
    }

    // 4. 应用你之前的 MarkdownStyle
    fun applyMarkdownStyle(style: MarkdownStyle) {
        val current = _textValue.value
        val next = style.apply(current)
        onContentChange(next)
    }
    private fun changePostUiState(uiState : PostCreateUiState) {
        savedStateHandle[POST_CREATE_STATE_KEY] = uiState
    }
    fun onTitleChanged(title : String) {
        savedStateHandle[TITLE_KEY] = title
    }

    @OptIn(ExperimentalTime::class)
    fun onCreatePost(
        inputStream: InputStream?,
        category : String,
        tags : List<WordTag> = emptyList(),
        wordName : String,
    ) {

        viewModelScope.launch {
            changePostUiState(PostCreateUiState.Loading)
            val fileName = "word_${Clock.System.now()}.md"
            try {
                inputStream?.use {ist ->
                    val bytes = ist.readBytes()
                    val wordUrl = uploadWordDomain(fileName = fileName,bytes = bytes)
                    if (wordUrl == null) {
                        return@launch
                    }
                    val isSuccess = wordRepository.createWord(
                        wordUrl = wordUrl,
                        category = category,
                        tags = tags,
                        wordName = wordName
                    )
                    if (isSuccess) {
                        changePostUiState(PostCreateUiState.Success)
                    }else {
                        changePostUiState(PostCreateUiState.Error)
                    }
                }

            }catch (e : Exception) {
                changePostUiState(PostCreateUiState.Error)
            }
        }
    }
}
sealed interface PostCreateUiState {
    data object Empty : PostCreateUiState
    data object Error : PostCreateUiState
    data object Loading : PostCreateUiState
    data object Success : PostCreateUiState
}

private const val POST_CREATE_STATE_KEY = "postCreateStateKey"
private const val TITLE_KEY = "titleKey"
private const val CATEGORY_KEY = "categoryKey"