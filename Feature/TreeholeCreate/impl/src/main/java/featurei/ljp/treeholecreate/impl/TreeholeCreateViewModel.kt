package featurei.ljp.treeholecreate.impl

import ai.ljp.data.repository.TreeholeRepository
import ai.ljp.data.repository.UserDataRepository
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ljp.model.MoodThemeConfig
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class TreeholeCreateViewModel @Inject constructor(
    private val savedStateHandle: SavedStateHandle,
    private val treeholeRepository: TreeholeRepository,
    private val userDataRepository: UserDataRepository
) : ViewModel(){
    val currentMood : StateFlow<MoodThemeConfig> = userDataRepository.userData.map {
        it.moodThemeConfig
    }.stateIn(
        scope = viewModelScope,
        initialValue = MoodThemeConfig.Normal,
        started = SharingStarted.WhileSubscribed(5_000)
    )
    val content : StateFlow<String> = savedStateHandle.getStateFlow(CONTENT_KEY,"")
    fun onCreateClick(content: String,isAnonymous : Boolean) {
        viewModelScope.launch {
            treeholeRepository.createTreehole(
                content,
                isAnonymous
            )
        }
    }

    fun onContentChanged(content : String) {
        savedStateHandle[CONTENT_KEY] = content
    }
}
private const val CONTENT_KEY = "contentKey"