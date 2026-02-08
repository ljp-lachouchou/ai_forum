package ai.ljp.ui

import androidx.paging.PagingData
import com.ljp.model.WordCommentsResource

sealed interface WordsFeedUiState {
    data object Loading : WordsFeedUiState
    data class Success(
        val feed : PagingData<WordCommentsResource>
    ) : WordsFeedUiState
}