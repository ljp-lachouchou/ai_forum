package feature.ljp.search.impl

import com.ljp.model.RecentSearchQuery

sealed interface SearchQueryUiState {
    data object Loading : SearchQueryUiState
    data class Success(
        val recentQueries : List<RecentSearchQuery> = emptyList()
    ) : SearchQueryUiState
}