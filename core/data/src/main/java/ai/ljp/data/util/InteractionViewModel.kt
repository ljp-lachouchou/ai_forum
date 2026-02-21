package ai.ljp.data.util

import ai.ljp.data.repository.UserDataRepository
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.filter
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

abstract class InteractionViewModel(
    private val userDataRepository: UserDataRepository
) : ViewModel() {

    private val likeStateCache = mutableMapOf<String, StateFlow<Boolean>>()
    private val bookmarkStateCache = mutableMapOf<String, StateFlow<Boolean>>()
    private val likeRefreshEvent = MutableSharedFlow<String>(extraBufferCapacity = 16)
    private val bookmarkRefreshEvent = MutableSharedFlow<String>(extraBufferCapacity = 16)
    val currentId : Flow<String> =
        userDataRepository.userData.map { it.currentUserId!! }

    protected abstract fun markBookmark(postId : String,
                                        userId : String) : Flow<Boolean>

    protected abstract fun markLike(postId : String,
                                        userId : String) : Flow<Boolean>
    protected abstract suspend fun toggleBookmarkActual(postId : String,userId : String)
    protected abstract suspend fun toggleLikeBookmarkActual(postId : String,userId : String)

    fun isLike(postId: String) : StateFlow<Boolean> =
        likeStateCache.getOrPut(postId) {
            combine(
                currentId,
                likeRefreshEvent
                    .filter { refreshPostId -> refreshPostId == postId }
                    .onStart { emit(postId) }
            ) { userId, _ -> userId }
                .flatMapLatest { userId ->
                    markLike(postId=postId,userId=userId)
                }
                .distinctUntilChanged()
                .stateIn(
                    scope = viewModelScope,
                    initialValue = false,
                    started = SharingStarted.WhileSubscribed(5_000)
                )
        }

    fun isBookmark(postId: String) : StateFlow<Boolean> =
        bookmarkStateCache.getOrPut(postId) {
            combine(
                currentId,
                bookmarkRefreshEvent
                    .filter { refreshPostId -> refreshPostId == postId }
                    .onStart { emit(postId) }
            ) { userId, _ -> userId }
                .flatMapLatest { userId ->
                    markBookmark(postId=postId,userId=userId)
                }
                .distinctUntilChanged()
                .stateIn(
                    scope = viewModelScope,
                    initialValue = false,
                    started = SharingStarted.WhileSubscribed(5_000)
                )
        }
    fun toggleBookmark(postId: String) {
        viewModelScope.launch {
            val userId = currentId.first()
            toggleBookmarkActual(postId,userId)
            bookmarkRefreshEvent.tryEmit(postId)
        }
    }

    fun toggleLike(postId: String) {
        viewModelScope.launch {
            val userId = currentId.first()
            toggleLikeBookmarkActual(postId,userId)
            likeRefreshEvent.tryEmit(postId)
        }
    }
}