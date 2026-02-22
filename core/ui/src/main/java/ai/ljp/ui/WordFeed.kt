package ai.ljp.ui

import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.lazy.staggeredgrid.LazyStaggeredGridScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.paging.PagingData
import androidx.paging.compose.LazyPagingItems
import androidx.paging.compose.itemKey
import com.ljp.model.WordCommentsResource
import kotlinx.coroutines.flow.Flow

fun LazyStaggeredGridScope.wordsFeed(
    wordsSource : LazyPagingItems<WordCommentsResource>,
    onProfileClick : (String) -> Unit,
    onPostClick : (String) -> Unit,
) {
    items(wordsSource.itemCount, key = wordsSource.itemKey { it.wordId }) {index ->
        val wordCommentsResource = wordsSource[index]
        if (wordCommentsResource != null) {
            WordCard(
                wordSource = wordCommentsResource,
                category = wordCommentsResource.category,
                onClick = onPostClick,
                onProfileClick = onProfileClick,
                modifier = Modifier.heightIn(max = 500.dp)
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