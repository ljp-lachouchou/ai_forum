package featurei.ljp.treeholecreate.impl

import ai.ljp.data.repository.ProfileRepository
import ai.ljp.data.repository.TreeholeRepository
import ai.ljp.data.repository.UserDataRepository
import ai.ljp.sync.status.SyncManager
import ai.ljp.ui.ProfileUiState
import android.os.Parcelable
import android.util.Log
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ljp.common.result.asResult
import com.ljp.model.MoodThemeConfig
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import kotlinx.parcelize.Parcelize
import javax.inject.Inject

@HiltViewModel
class TreeholeCreateViewModel @Inject constructor(
    private val savedStateHandle: SavedStateHandle,
    private val treeholeRepository: TreeholeRepository,
    private val profileRepository: ProfileRepository,
    private val userDataRepository: UserDataRepository,
    private val workManager: SyncManager
) : ViewModel(){
    val currentUserId : Flow<String> =
        userDataRepository.userData.map { it.currentUserId!! }
    val createTreeholeUiState : StateFlow<CreateTreeholeUiState> =
        savedStateHandle.getStateFlow(
            UI_STATE_KEY,
            CreateTreeholeUiState.Idle
        )
    val content : StateFlow<String> = savedStateHandle.getStateFlow(CONTENT_KEY,"")
    val profileUiState : StateFlow<ProfileUiState> = currentUserId.flatMapLatest { profileId ->
        profileUiState(
            profileId = profileId,
            profileRepository = profileRepository
        )
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5_000),
        initialValue = ProfileUiState.Loading
    )
    fun onCreateClick(content: String,isAnonymous : Boolean,moodThemeConfig: MoodThemeConfig) {
        viewModelScope.launch {
            savedStateHandle[UI_STATE_KEY] = CreateTreeholeUiState.Loading
            val created = treeholeRepository.createTreehole(
                content,
                isAnonymous,
                MoodThemeConfig.fromMood(moodThemeConfig)
            )
            if (created) {
                workManager.requestSync()
                savedStateHandle[UI_STATE_KEY] = CreateTreeholeUiState.Success
            }else {
                savedStateHandle[UI_STATE_KEY] = CreateTreeholeUiState.Error
            }
        }
    }
    val isAnonymous : StateFlow<Boolean> = savedStateHandle.getStateFlow(ANONYMOUS_KEY,true)
    fun onAnonymousChanged() {
        val current = savedStateHandle.get<Boolean>(ANONYMOUS_KEY) ?: true
        savedStateHandle[ANONYMOUS_KEY] = !current
    }
    fun onContentChanged(content : String) {
        savedStateHandle[CONTENT_KEY] = content
    }

}
@Parcelize
sealed interface CreateTreeholeUiState : Parcelable {
    @Parcelize
    data object Idle : CreateTreeholeUiState
    @Parcelize
    data object Loading : CreateTreeholeUiState
    @Parcelize
    data object Error : CreateTreeholeUiState
    @Parcelize
    data object Success : CreateTreeholeUiState
}
private fun profileUiState(
    profileId : String,
    profileRepository: ProfileRepository
) : Flow<ProfileUiState> {
    val profileStream = profileRepository.getProfile(profileId)
    return profileStream.asResult()
        .map { profileResult ->
            when(profileResult) {
                is com.ljp.common.result.Result.Success -> {
                    val result = profileResult.data
                    ProfileUiState.Success(profile = result)
                }
                is com.ljp.common.result.Result.Loading -> ProfileUiState.Loading
                is com.ljp.common.result.Result.Error -> ProfileUiState.Error
            }

        }

}

private const val CONTENT_KEY = "contentKey"
private const val ANONYMOUS_KEY = "anonymousKey"

private const val UI_STATE_KEY = "uiStateKey"