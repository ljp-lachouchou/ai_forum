package feature.ljp.search.impl

import ai.ljp.data.model.AISearch
import ai.ljp.data.model.isEmpty

sealed interface SearchResultUiState {
    data object Loading : SearchResultUiState
    data object EmptyQuery : SearchResultUiState

    data object LoadFailed : SearchResultUiState
    data class Success(
        val aiSearch: AISearch
    ) : SearchResultUiState {
        fun isEmpty() = aiSearch.isEmpty()
    }
}