package feature.ljp.postcreate.impl

import ai.ljp.data.repository.UserDataRepository
import ai.ljp.data.repository.WordRepository
import ai.ljp.designsystem.component.markdown.render.tool.MarkdownStyle
import ai.ljp.domain.UploadWordDomain
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
import javax.inject.Inject

@HiltViewModel
class PostCreateViewModel @Inject constructor(
    private val wordRepository: WordRepository,
    private val uploadWordDomain: UploadWordDomain,
    private val savedStateHandle: SavedStateHandle
) : ViewModel() {

    private val _textValue = MutableStateFlow(
        TextFieldValue("# ")
    )
    val textValue: StateFlow<TextFieldValue> = _textValue.asStateFlow()

    val postCreateUiState : StateFlow<PostCreateUiState> =
        savedStateHandle.getStateFlow(POST_CREATE_STATE_KEY,
            PostCreateUiState.Empty)

    // 2. 当用户输入或点击工具栏时，调用此方法
    fun onContentChange(newVal: TextFieldValue) {
        _textValue.value = newVal
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
    fun onCreatePost(
        fileName : String,
        bytes : ByteArray,
        category : String,
        tags : List<WordTag> = emptyList(),
        wordName : String,
    ) {
        viewModelScope.launch {
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
    }
}
sealed interface PostCreateUiState {
    data object Empty : PostCreateUiState
    data object Error : PostCreateUiState
    data object Success : PostCreateUiState
}

private const val POST_CREATE_STATE_KEY = "postCreateStateKey"