package feature.ljp.home.impl

import ai.ljp.analytics.AnalyticsHelper
import ai.ljp.data.repository.BookmarkRepository
import ai.ljp.data.repository.LikeRepository
import ai.ljp.data.repository.UserDataRepository
import ai.ljp.domain.UploadWordDomain
import ai.ljp.sync.status.SyncManager
import ai.ljp.ui.WordsFeedUiState
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val savedStateHandle : SavedStateHandle,
    syncManager : SyncManager,
    private val analyticsHelper: AnalyticsHelper,
    private val userDataRepository: UserDataRepository,
    private val bookmarkRepository: BookmarkRepository,
    private val likeRepository: LikeRepository,
    private val updateWordDomain: UploadWordDomain,
) : ViewModel() {
    val currentId : Flow<String> =
        userDataRepository.userData.map { it.currentUserId!! }
    val isSyncing =syncManager.isSyncing
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5_000),
            initialValue = false
        )
    val feedUiState =
        updateWordDomain.observerAllWords()
            .map {
                WordsFeedUiState.Success(flowOf(it))
            }
            .stateIn(
                scope = viewModelScope,
                started = SharingStarted.WhileSubscribed(5_000),
                initialValue = WordsFeedUiState.Loading
            )
    fun isLike(postId: String) : StateFlow<Boolean> =
        currentId.flatMapLatest { userId ->
            likeRepository.markLike(postId=postId,userId=userId)
        }.stateIn(
            scope = viewModelScope,
            initialValue = false,
            started = SharingStarted.WhileSubscribed(5_000)
        )
    fun isBookmark(postId: String) : StateFlow<Boolean> =
        currentId.flatMapLatest { userId ->
            bookmarkRepository.markBookmark(postId=postId,userId=userId)
        }.stateIn(
            scope = viewModelScope,
            initialValue = false,
            started = SharingStarted.WhileSubscribed(5_000)
        )
    fun toggleLike(postId : String) {
        viewModelScope.launch {
            val userId = currentId.first()
            likeRepository.toggleLike(userId = userId, postId = postId)
        }
    }
    fun toggleBookmark(postId: String) {
        viewModelScope.launch {
            val userId = currentId.first()
            bookmarkRepository.toggleBookmark(userId = userId,postId=postId)
        }
    }
}