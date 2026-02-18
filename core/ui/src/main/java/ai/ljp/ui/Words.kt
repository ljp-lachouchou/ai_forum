package ai.ljp.ui

import ai.ljp.designsystem.component.AIForumToggleButton
import ai.ljp.designsystem.component.DynamicContent
import ai.ljp.designsystem.icon.AIForumIcon
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
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
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.paging.PagingData
import androidx.paging.compose.LazyPagingItems
import androidx.paging.compose.itemKey
import com.ljp.common.baseui.markdown.render.actual.MarkdownView
import com.ljp.model.WordCommentsResource
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.datetime.Instant

sealed interface WordsUiState {
    data object Loading : WordsUiState
    data object Error : WordsUiState

    data class Success(val wordsSource : Flow<PagingData<WordCommentsResource>>) : WordsUiState
}


@Composable
fun WordCardItem(
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
                        MarkdownView(input = it)
                    }
                )
            }
            InteractionArea(
                likes = likes,
                comments = comments,
                bookmarks = bookmarks,
                postId = wordId
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
    id : String? = null,
    enabled : Boolean = true,
    checkIcon : ImageVector? = null,
    isChecked : ((String) -> StateFlow<Boolean>)? = null,
    onCheckClick : (String) -> Unit = {},
    color : Color = MaterialTheme.colorScheme.onPrimaryContainer
) {
    val checked = if (id != null && isChecked != null) {
        isChecked(id).collectAsStateWithLifecycle()
    }else {
        remember { mutableStateOf(false) }
    }
    Row(
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(2.dp)
    ) {
        AIForumToggleButton(
            checked = checked.value,
            onCheckedChange = {
                if (id != null) {
                    onCheckClick(id)
                }
            },
            icon = {
                Icon(imageVector = icon, contentDescription = null, tint = color)
            },
            checkedIcon = {
                if (checkIcon != null) {
                    Icon(imageVector = checkIcon, contentDescription = null, tint = color)
                }
            }, enabled = enabled
        )
        ProvideTextStyle(MaterialTheme.typography.bodySmall) {
            Text(text = content, color = color)
        }
    }
}
@Composable
fun InteractionArea(
    postId : String,
    likes : Int,
    comments : Int,
    bookmarks : Int,
    modifier: Modifier = Modifier,
    onLikeClick : (String) -> Unit = {},
    onBookmarkClick : (String) -> Unit = {},
    isLike : ((String) -> StateFlow<Boolean>)? = null,
    isBookmark : ((String) -> StateFlow<Boolean>)? = null,

) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(4.dp),
        modifier = modifier
    ) {
        InteractionItem(
            icon = AIForumIcon.OutlineLike,
            content = "$likes",
            onCheckClick = onLikeClick,
            isChecked = isLike,
            checkIcon = AIForumIcon.Like,
            id = postId
        )
        InteractionItem(
            AIForumIcon.OutlineChat,
            "$comments",
            enabled = false
        )
        InteractionItem(
            AIForumIcon.OutlineBookmark,
            "$bookmarks",
            onCheckClick = onBookmarkClick,
            isChecked = isBookmark,
            checkIcon = AIForumIcon.Bookmark,
            id = postId
        )
    }
}

