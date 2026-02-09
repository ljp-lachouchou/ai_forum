package ai.ljp.ui

import ai.ljp.designsystem.component.AIForumToggleButton
import ai.ljp.designsystem.component.DynamicAsyncImage
import ai.ljp.designsystem.component.DynamicContent
import ai.ljp.designsystem.icon.AIForumIcon
import ai.ljp.designsystem.theme.LocalTintTheme
import ai.ljp.designsystem.theme.TintTheme
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.IconButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ProvideTextStyle
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import com.ljp.model.Profile
import com.ljp.model.WordCommentsResource

@Composable
fun WordCard(
    wordSource : WordCommentsResource,
    bookmarked : Boolean,
    isLike : Boolean,
    category : String,
    onToggleBookmark :(String) -> Unit,
    onClick : (String) -> Unit,
    onProfileClick : (String) -> Unit,
    onToggleLike : (String) -> Unit,
    modifier: Modifier = Modifier
) {
    Card(
        onClick = {
            onClick(wordSource.wordId)
        },
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
        modifier = modifier
    ) {
        Column {
            WordCardHead(category = category, bookmarked = bookmarked,
                onToggleBookmark = {
                    onToggleBookmark(wordSource.wordId)
                })
            Spacer(Modifier.height(3.dp))
            ProvideTextStyle(MaterialTheme.typography.headlineLarge) {
                Text(text = wordSource.wordName)
            }
            Box(modifier = Modifier.padding(3.dp)) {
                DynamicContent(
                    url = wordSource.wordUrl,
                ) {content ->
                    ProvideTextStyle(MaterialTheme.typography.bodySmall) {
                        Text(text = content,
                            maxLines = 3, overflow = TextOverflow.Ellipsis)
                    }
                }
            }
            HorizontalDivider()
            WordCardTail(author = wordSource.author,
                isLike = isLike,
                onToggleLike = {
                    onToggleLike(wordSource.wordId)
                },
                onProfileClick = onProfileClick,
            )
        }
    }
}

@Composable
private fun WordCardHead(
    category : String,
    bookmarked: Boolean,
    onToggleBookmark :() -> Unit,
) {
    val tint = LocalTintTheme.current.iconTint
    Row(
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        ProvideTextStyle(MaterialTheme.typography.headlineMedium) {
            Text(text = category)
        }
        AIForumToggleButton(
            checked = bookmarked,
            onCheckedChange = {
                onToggleBookmark()
            },
            icon = {
                Icon(
                    imageVector = AIForumIcon.Bookmarks,
                    contentDescription = "bookmark",
                    tint = Color.Transparent
                )

            },
            checkedIcon = {
                Icon(
                    imageVector = AIForumIcon.Bookmarks,
                    contentDescription = "bookmark",
                    tint = if (tint == Color.Unspecified) {
                        MaterialTheme.colorScheme.onPrimary
                    }else {
                        tint
                    }
                )
            }
        )
    }
}
@Composable
private fun WordCardTail(
    author : Profile,
    isLike : Boolean,
    onToggleLike: () -> Unit,
    onProfileClick: (String) -> Unit,

    ) {
    val tint = LocalTintTheme.current.iconTint
    Row(
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        ProfileHead(profile = author,
            modifier = Modifier.weight(0.7F),
            onProfileClick = onProfileClick)
        AIForumToggleButton(
            checked = isLike,
            onCheckedChange = {
                onToggleLike()
            },
            icon = {
                Icon(
                    imageVector = AIForumIcon.Bookmarks,
                    contentDescription = "bookmark",
                    tint = Color.Transparent
                )
            },
            checkedIcon = {
                Icon(
                    imageVector = AIForumIcon.Bookmarks,
                    contentDescription = "bookmark",
                    tint = if (tint == Color.Unspecified) {
                        MaterialTheme.colorScheme.onPrimary
                    }else {
                        tint
                    }
                )
            }
        )
    }
}
@Composable
internal fun ProfileHead(
    profile : Profile,
    modifier: Modifier = Modifier,
    onProfileClick : (String) -> Unit
) {
    Row(verticalAlignment = Alignment.CenterVertically, modifier = modifier.clickable {
        onProfileClick(profile.id)
    }) {
        DynamicAsyncImage(
            imageUrl = profile.avatarUrl,
            contentDescription = "profile",
        )
        ProvideTextStyle(MaterialTheme.typography.headlineSmall) {
            Text(text = profile.userName)
        }
    }

}