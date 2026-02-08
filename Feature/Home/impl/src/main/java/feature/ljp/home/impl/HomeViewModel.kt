package feature.ljp.home.impl

import ai.ljp.analytics.AnalyticsHelper
import ai.ljp.data.repository.BookmarkRepository
import ai.ljp.data.repository.LikeRepository
import ai.ljp.data.repository.UserDataRepository
import ai.ljp.data.repository.WordRepository
import ai.ljp.domain.UploadWordDomain
import ai.ljp.sync.status.SyncManager
import ai.ljp.ui.WordsFeedUiState
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val savedStateHandle : SavedStateHandle,
    syncManager : SyncManager,
    private val analyticsHelper: AnalyticsHelper,
    private val userDataRepository: UserDataRepository,
    private val bookmarkRepository: BookmarkRepository,
    private val likeRepository: LikeRepository,
    private val updateWordDomain: UploadWordDomain
) : ViewModel() {
    val isSyncing =syncManager.isSyncing
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5_000),
            initialValue = false
        )
    val feedUiState =
        updateWordDomain.observerAllWords()
            .map(WordsFeedUiState::Success)
            .stateIn(
                scope = viewModelScope,
                started = SharingStarted.WhileSubscribed(5_000),
                initialValue = WordsFeedUiState.Loading
            )


}