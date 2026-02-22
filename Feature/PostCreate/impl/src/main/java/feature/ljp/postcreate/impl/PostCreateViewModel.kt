package feature.ljp.postcreate.impl

import ai.ljp.data.repository.WordRepository
import ai.ljp.designsystem.component.ImagePreprocessor
import ai.ljp.domain.UploadProfileDomain
import ai.ljp.domain.UploadWordDomain
import ai.ljp.sync.status.SyncManager
import ai.ljp.ui.Category
import android.content.Context
import android.net.Uri
import android.os.Parcelable
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ljp.model.WordTag
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import kotlinx.parcelize.Parcelize
import java.io.InputStream
import java.lang.Exception
import javax.inject.Inject
import kotlin.time.Clock
import kotlin.time.ExperimentalTime

@HiltViewModel
class PostCreateViewModel @Inject constructor(
    private val wordRepository: WordRepository,
    private val uploadWordDomain: UploadWordDomain,
    private val uploadProfileDomain: UploadProfileDomain,
    private val savedStateHandle: SavedStateHandle,
    private val workManagerSyncManager: SyncManager
) : ViewModel() {
    val title : StateFlow<String> =
        savedStateHandle.getStateFlow(TITLE_KEY,"")
    val postCreateUiState : StateFlow<PostCreateUiState> =
        savedStateHandle.getStateFlow(POST_CREATE_STATE_KEY,
            PostCreateUiState.Empty)
    val category : StateFlow<Int> =
        savedStateHandle.getStateFlow(CATEGORY_KEY, Category.Tech.ordinal)
    // 2. 当用户输入或点击工具栏时，调用此方法

    fun onCategoryOrdinalChanged(category: Category) {
        savedStateHandle[CATEGORY_KEY] = category.ordinal
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
                        changePostUiState(PostCreateUiState.Error)
                        return@launch
                    }
                    val isSuccess = wordRepository.createWord(
                        wordUrl = wordUrl,
                        category = category,
                        tags = tags,
                        wordName = wordName
                    )
                    if (isSuccess) {
                        workManagerSyncManager.requestSync()
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

    @OptIn(ExperimentalTime::class)
    fun onUploadImage(context : Context, uri : Uri,block : (String) -> Unit) {
        viewModelScope.launch {
            val prepared = ImagePreprocessor.prepare(context, uri)
            if (prepared == null) {
                return@launch
            }
            val fileName = "image_${Clock.System.now()}.${prepared.extension}"
            val url = withContext(Dispatchers.IO) {
                uploadProfileDomain(fileName, prepared.bytes)
            }
            url?.let(block)
        }
    }
}
@Parcelize
sealed interface PostCreateUiState : Parcelable {
    @Parcelize
    data object Empty : PostCreateUiState
    @Parcelize
    data object Error : PostCreateUiState
    @Parcelize
    data object Loading : PostCreateUiState
    @Parcelize
    data object Success : PostCreateUiState
}

private const val POST_CREATE_STATE_KEY = "postCreateStateKey"
private const val TITLE_KEY = "titleKey"
private const val CATEGORY_KEY = "categoryKey"
private const val CUR_IMAGE_KEY = "curImageKey"