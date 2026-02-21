package feature.ljp.post.impl

import ai.ljp.data.repository.BookmarkRepository
import ai.ljp.data.repository.CommentRepository
import ai.ljp.data.repository.InteractionWordRepository
import ai.ljp.data.repository.LikeRepository
import ai.ljp.data.repository.UserDataRepository
import ai.ljp.data.util.InteractionViewModel
import ai.ljp.sync.status.SyncManager
import android.util.Log
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.ljp.common.result.Result
import com.ljp.common.result.asResult
import com.ljp.model.CommentProfileResource
import com.ljp.model.WordCommentsResource
import dagger.assisted.Assisted
import dagger.assisted.AssistedFactory
import dagger.assisted.AssistedInject
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.filter
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

@HiltViewModel(assistedFactory = PostViewModel.Factory::class)
class PostViewModel @AssistedInject constructor(
    private val savedStateHandle: SavedStateHandle,
    @Assisted val postId : String,
    private val interactionWordRepository: InteractionWordRepository,
    private val bookmarkRepository: BookmarkRepository,
    private val likeRepository: LikeRepository,
    private val commentRepository: CommentRepository,
    private val userDataRepository: UserDataRepository,
    private val workManager: SyncManager
) : InteractionViewModel(userDataRepository) {
    val commentContent =savedStateHandle.getStateFlow(COMMENT_CONTENT_QUERY,"")
    val postUiState : StateFlow<PostUiState> = postUiState(
        postId = postId,
        interactionWordRepository = interactionWordRepository
    )
        .stateIn(
            scope = viewModelScope,
            initialValue = PostUiState.Loading,
            started = SharingStarted.WhileSubscribed(5_000)
        )
    val commentsFeedUiState : StateFlow<CommentsFeedUiState> = commentsFeedUiState(
        postId = postId,
        commentRepository = commentRepository,
        scope = viewModelScope
    )
        .stateIn(
            scope = viewModelScope,
            initialValue = CommentsFeedUiState.Loading,
            started = SharingStarted.WhileSubscribed(5_000)
        )
    fun onCommentContentChanged(content : String) {
        savedStateHandle[COMMENT_CONTENT_QUERY] = content
    }

    fun createComment(postId : String,content : String) {
        viewModelScope.launch {
            val success = commentRepository.createComment(
                postId = postId,
                content = content
            )
            if (success) {
                workManager.requestSync()
            }
        }
    }

    override fun markBookmark(
        postId: String,
        userId: String
    ): Flow<Boolean> =
        bookmarkRepository.markBookmark(postId,userId)

    override fun markLike(
        postId: String,
        userId: String
    ): Flow<Boolean> = likeRepository.markLike(postId,userId)

    override suspend fun toggleBookmarkActual(postId: String, userId: String) {
        bookmarkRepository.toggleBookmark(userId = userId,postId=postId)
    }

    override suspend fun toggleLikeBookmarkActual(postId: String, userId: String) {
        likeRepository.toggleLike(userId=userId,postId=postId)
    }

    @AssistedFactory
    interface Factory {
        fun create(postId : String) : PostViewModel
    }
}

private fun postUiState(
    postId: String,
    interactionWordRepository: InteractionWordRepository
) : Flow<PostUiState> {
    val postStream = interactionWordRepository.getPost(wordId = postId)
    return postStream.asResult()
        .map { postResult ->
            when(postResult) {
                is Result.Success -> PostUiState.Success(post = postResult.data)
                is Result.Error -> PostUiState.Error
                is Result.Loading -> PostUiState.Loading
            }
        }
}
private fun commentsFeedUiState(
    postId: String,
    commentRepository: CommentRepository,
    scope: CoroutineScope
) : Flow<CommentsFeedUiState> {
    val feed = commentRepository.getCommentsProfileResource(postId)
    return feed
        .cachedIn(scope = scope)
        .asResult()
        .map { feedResult ->
            when(feedResult) {
                is Result.Error -> CommentsFeedUiState.Error
                is Result.Loading -> CommentsFeedUiState.Loading
                is Result.Success -> {
                    CommentsFeedUiState.Success(feed = flowOf(feedResult.data))
                }
            }
        }
}
sealed interface PostUiState {
    data class Success(val post : WordCommentsResource) : PostUiState
    data object Loading : PostUiState
    data object Error : PostUiState
}
sealed interface CommentsFeedUiState {
    data object Loading : CommentsFeedUiState
    data object Error : CommentsFeedUiState

    data class Success(val feed : Flow<PagingData<CommentProfileResource>>) : CommentsFeedUiState
}
private const val COMMENT_CONTENT_QUERY = "commentContent"
