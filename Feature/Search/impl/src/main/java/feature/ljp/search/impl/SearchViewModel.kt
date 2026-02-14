package feature.ljp.search.impl

import ai.ljp.data.model.isEmpty
import ai.ljp.data.model.isNotOk
import ai.ljp.data.repository.InteractionWordRepository
import ai.ljp.data.repository.RecentSearchRepository
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SearchViewModel @Inject constructor(
    private val savedStateHandle: SavedStateHandle,
    private val recentSearchRepository: RecentSearchRepository,
    private val interactionWordRepository: InteractionWordRepository
) : ViewModel() {
    val searchQuery = savedStateHandle.getStateFlow(SEARCH_KEY,"")

    val recentQueryUiState : StateFlow<SearchQueryUiState> =
        recentSearchRepository.getRecentSearchQueries(limit = 10)
            .map(SearchQueryUiState::Success)
            .stateIn(
                scope = viewModelScope,
                started = SharingStarted.WhileSubscribed(5_000),
                initialValue = SearchQueryUiState.Loading,
            )
    val searchResultUiState = searchQuery.flatMapLatest {query->
        if (query.trim().length < SEARCH_QUERY_MIN_LENGTH) {
            flowOf(SearchResultUiState.EmptyQuery)
        }else {
            val aiSearch = interactionWordRepository.aiSearch(query)
            if (aiSearch.isNotOk()) {
                flowOf(SearchResultUiState.LoadFailed)
            }else {
                flowOf(SearchResultUiState.Success(aiSearch))
            }
        }

    }
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5_000),
            initialValue = SearchResultUiState.Loading,
        )

    fun onSearchQueryChanged(query: String) {
        savedStateHandle[SEARCH_KEY] = query
    }
    fun onSearchTriggered(query: String) {
        if (query.isBlank()) return
        viewModelScope.launch {
            recentSearchRepository.insertOrReplaceRecentSearch(searchQuery = query)
        }
    }
    fun clearRecentSearches() {
        viewModelScope.launch {
            recentSearchRepository.clearRecentSearches()
        }
    }
}
private const val SEARCH_QUERY_MIN_LENGTH = 2
private const val SEARCH_KEY = "searchKey"