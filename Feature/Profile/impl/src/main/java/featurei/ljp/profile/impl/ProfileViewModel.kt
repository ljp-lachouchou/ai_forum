package featurei.ljp.profile.impl

import ai.ljp.data.repository.InteractionWordRepository
import ai.ljp.data.repository.ProfileRepository
import ai.ljp.ui.ProfileUiState
import ai.ljp.ui.WordsUiState
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ljp.common.result.Result
import com.ljp.common.result.asResult
import com.ljp.model.Profile
import dagger.assisted.Assisted
import dagger.assisted.AssistedFactory
import dagger.assisted.AssistedInject
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn

@HiltViewModel(assistedFactory = ProfileViewModel.Factory::class)
class ProfileViewModel @AssistedInject constructor(
    private val savedStateHandle: SavedStateHandle,
    private val profileRepository: ProfileRepository,
    private val interactionWordRepository: InteractionWordRepository,
    @Assisted val profileId : String
) : ViewModel() {
    val profileUiState : StateFlow<ProfileUiState> = profileUiState(
        profileId = profileId, 
        profileRepository = profileRepository
    )
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5_000),
            initialValue = ProfileUiState.Loading
        )
    val wordsUiState : StateFlow<WordsUiState> = wordsUiState(
        profileId = profileId,
        interactionWordRepository = interactionWordRepository
    )
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5_000),
            initialValue = WordsUiState.Loading
        )
    @AssistedFactory
    interface Factory {
        fun create(profileId : String) : ProfileViewModel
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
private fun wordsUiState(
    profileId : String,
    interactionWordRepository: InteractionWordRepository,
) : Flow<WordsUiState>{
    val flowPagingData = interactionWordRepository.getSelfWords(profileId)
    return flowPagingData.asResult()
        .map { dataResult->
            when(dataResult) {
                is Result.Success -> {
                    val rs = dataResult.data
                    WordsUiState.Success(flowOf(rs))
                }
                is Result.Loading -> WordsUiState.Loading
                is Result.Error -> WordsUiState.Error
            }
        }

}

