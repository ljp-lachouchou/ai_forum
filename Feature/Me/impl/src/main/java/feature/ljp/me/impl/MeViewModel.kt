package feature.ljp.me.impl

import ai.ljp.data.repository.ProfileRepository
import ai.ljp.data.repository.UserDataRepository
import ai.ljp.designsystem.theme.Mood
import ai.ljp.ui.ProfileUiState
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import com.ljp.common.result.asResult
import com.ljp.model.MoodThemeConfig
import com.ljp.model.Profile
import com.ljp.model.WordCommentsResource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MeViewModel @Inject constructor(
    private val savedStateHandle: SavedStateHandle,
    private val userDataRepository: UserDataRepository,
    private val profileRepository: ProfileRepository
) : ViewModel() {
    val currentMood : StateFlow<MoodThemeConfig> =
        userDataRepository.userData.map { it.moodThemeConfig }
            .stateIn(
                scope = viewModelScope,
                started = SharingStarted.WhileSubscribed(5_000),
                initialValue = MoodThemeConfig.Normal
            )
    val currentUserId : Flow<String> =
        userDataRepository.userData.map { it.currentUserId!! }
    val profileUiState : StateFlow<ProfileUiState> =
        currentUserId.flatMapLatest { profileId ->
            profileUiState(
                profileId = profileId,
                profileRepository = profileRepository
            )
        }.stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5_000),
            initialValue = ProfileUiState.Loading
        )
    fun moodChanged(mood : MoodThemeConfig) {
        viewModelScope.launch {
            userDataRepository.setMoodThemeConfig(mood)
        }
    }
    val currentActionState : StateFlow<ActionState> =
        savedStateHandle.getStateFlow(ACTION_KEY, ActionState.Empty)
    fun actionChanged(actionState: ActionState) {
        savedStateHandle[ACTION_KEY] = actionState
    }


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
private fun selfPostFeedUiState(postIds : List<String>) {

}
private const val ACTION_KEY = "actionKey"
sealed interface ActionState {
    data object Empty : ActionState
    data object MyPost : ActionState
    data object UpdateProfile : ActionState
    data object BookmarksPost : ActionState
    data object LikesPost : ActionState
    data object Settings : ActionState
}
internal sealed interface SelfProfileUiState {
    data object Loading : SelfProfileUiState
    data object Error : SelfProfileUiState
    data class Success(val profile : Profile) : SelfProfileUiState
}
