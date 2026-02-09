package ai.ljp.ui

import androidx.compose.foundation.lazy.staggeredgrid.LazyStaggeredGridScope
import androidx.compose.foundation.lazy.staggeredgrid.items
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.paging.PagingData
import androidx.paging.compose.collectAsLazyPagingItems
import com.ljp.model.WordCommentsResource
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.StateFlow

fun LazyStaggeredGridScope.wordsFeed(
    wordsSource : List<WordCommentsResource>,
    onProfileClick : (String) -> Unit,
    onPostClick : (String) -> Unit,
    onToggleLikeClick : (String) -> Unit,
    onToggleBookmarkClick : (String) -> Unit,
    isLike : (String) -> StateFlow<Boolean>,
    isBookmark : (String) -> StateFlow<Boolean>,
) {
    items(wordsSource, key = {
        it.wordId
    }) {wordCommentsResource ->
        val liked by isLike(wordCommentsResource.wordId).collectAsState()
        val bookmarked by isBookmark(wordCommentsResource.wordId).collectAsState()
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
sealed interface WordsFeedUiState {
    data object Loading : WordsFeedUiState
    data class Success(
        val feed : Flow<PagingData<WordCommentsResource>>
    ) : WordsFeedUiState
}