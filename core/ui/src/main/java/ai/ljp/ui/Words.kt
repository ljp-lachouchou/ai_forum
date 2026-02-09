package ai.ljp.ui

import ai.ljp.designsystem.component.DynamicContent
import ai.ljp.designsystem.icon.AIForumIcon
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.lazy.LazyListScope
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ProvideTextStyle
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.unit.dp
import androidx.paging.PagingData
import androidx.paging.compose.LazyPagingItems
import androidx.paging.compose.itemKey
import com.ljp.model.WordCommentsResource
import kotlinx.coroutines.flow.Flow
import kotlinx.datetime.Instant

sealed interface WordsUiState {
    data object Loading : WordsUiState
    data object Error : WordsUiState

    data class Success(val wordsSource : Flow<PagingData<WordCommentsResource>>) : WordsUiState
}

fun LazyListScope.wordsItem(
    wordsLazyItems : LazyPagingItems<WordCommentsResource>,
    onPostClick : (String) -> Unit,
    modifier: Modifier = Modifier
) {
    items(wordsLazyItems.itemCount, key = wordsLazyItems.itemKey { it.wordId }) {index ->
        val word = wordsLazyItems[index]
        if (word != null) {
            WordCardItem(
                wordId = word.wordId,
                category = word.category,
                createdAt = word.createdAt,
                wordName = word.wordName,
                wordUrl = word.wordUrl,
                likes = word.likes.size,
                comments = word.comments.size,
                bookmarks = word.bookMarks.size,
                onPostClick = onPostClick,
                modifier = modifier
                    .fillMaxWidth()
            )
        }
    }
}
@Composable
private fun WordCardItem(
    wordId : String,
    category: String,
    createdAt: Instant,
    wordName : String,
    wordUrl : String,
    likes : Int,
    comments: Int,
    bookmarks: Int,
    onPostClick: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    Card(
        shape = RoundedCornerShape(20.dp),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.primaryContainer
        ),
        modifier = modifier
            .clickable {
                onPostClick(wordId)
            }
    ) {
        Column(
            verticalArrangement = Arrangement.spacedBy(4.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            ProfileWordHead(
                category = category,
                createdAt = createdAt
            )
            ProvideTextStyle(MaterialTheme.typography.headlineMedium) {
                Text(text = wordName)
            }
            ProvideTextStyle(MaterialTheme.typography.bodySmall) {
                DynamicContent(
                    url = wordUrl,
                    text = {
                        ProvideTextStyle(MaterialTheme.typography.bodySmall) {
                            Text(text = it)
                        }
                    }
                )
            }
            InteractionArea(
                likes = likes,
                comments = comments,
                bookmarks = bookmarks
            )
        }
    }
}
@Composable
private fun ProfileWordHead(
    category: String,
    createdAt : Instant,
    modifier: Modifier = Modifier
) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween,
        modifier = modifier.fillMaxWidth()
    ) {
        ProvideTextStyle(MaterialTheme.typography.headlineSmall) {
            Text(text = category)
        }
        ProvideTextStyle(MaterialTheme.typography.headlineSmall) {
            Text(text = createdAt.toString())
        }
    }
}
@Composable
private fun InteractionItem(
    icon : ImageVector,
    content : String,
    color : Color = MaterialTheme.colorScheme.onPrimaryContainer
) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(2.dp)
    ) {
        Icon(imageVector = icon, contentDescription = null, tint = color)
        ProvideTextStyle(MaterialTheme.typography.bodySmall) {
            Text(text = content, color = color)
        }
    }
}
@Composable
private fun InteractionArea(
    likes : Int,
    comments : Int,
    bookmarks : Int,
    modifier: Modifier = Modifier
) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(4.dp),
        modifier = modifier
    ) {
        InteractionItem(AIForumIcon.OutlineLike,"$likes")
        InteractionItem(AIForumIcon.OutlineChat,"$comments")
        InteractionItem(AIForumIcon.OutlineBookmark,"$bookmarks")
    }
}

