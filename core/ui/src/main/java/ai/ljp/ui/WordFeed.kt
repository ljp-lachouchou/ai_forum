package ai.ljp.ui

import androidx.compose.foundation.lazy.staggeredgrid.LazyStaggeredGridScope
import androidx.compose.runtime.getValue
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.paging.PagingData
import androidx.paging.compose.LazyPagingItems
import androidx.paging.compose.itemKey
import com.ljp.model.WordCommentsResource
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.StateFlow

fun LazyStaggeredGridScope.wordsFeed(
    wordsSource : LazyPagingItems<WordCommentsResource>,
    onProfileClick : (String) -> Unit,
    onPostClick : (String) -> Unit,
    onToggleLikeClick : (String) -> Unit,
    onToggleBookmarkClick : (String) -> Unit,
    isLike : (String) -> StateFlow<Boolean>,
    isBookmark : (String) -> StateFlow<Boolean>,
) {
    items(wordsSource.itemCount, key = wordsSource.itemKey { it.wordId }) {index ->
        val wordCommentsResource = wordsSource[index]
        if (wordCommentsResource != null) {
            val liked by isLike(wordCommentsResource.wordId).collectAsStateWithLifecycle()
            val bookmarked by isBookmark(wordCommentsResource.wordId).collectAsStateWithLifecycle()
            WordCard(
                wordSource = wordCommentsResource,
                bookmarked = bookmarked,
                isLike = liked,
                category = wordCommentsResource.category,
                onToggleBookmark = onToggleBookmarkClick,
                onClick = onPostClick,
                onProfileClick = onProfileClick,
                onToggleLike = onToggleLikeClick
            )
        }

    }
}
sealed interface WordsFeedUiState {
    data object Loading : WordsFeedUiState
    data class Success(
        val feed : Flow<PagingData<WordCommentsResource>>
    ) : WordsFeedUiState
}