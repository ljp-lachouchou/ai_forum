package ai.ljp.ui

import com.ljp.model.Profile
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map


sealed interface ProfileUiState {
    data class Success(val profile : Profile) : ProfileUiState
    data object Loading : ProfileUiState
    data object Error : ProfileUiState
}