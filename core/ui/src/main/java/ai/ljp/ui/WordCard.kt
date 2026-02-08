package ai.ljp.ui

import ai.ljp.designsystem.icon.AIForumIcon
import ai.ljp.designsystem.theme.LocalTintTheme
import ai.ljp.designsystem.theme.TintTheme
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
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
import androidx.compose.ui.unit.dp
import com.ljp.model.WordCommentsResource

@Composable
fun WordCard(
    wordSource : WordCommentsResource,
    bookmarked : Boolean,
    likes : Int,
    category : String,
    onToggleBookmark :() -> Unit,
    onClick : () -> Unit,
    onProfileClick : (String) -> Unit,
    onToggleLike : () -> Unit,
    modifier: Modifier = Modifier
) {
    Card(
        onClick = onClick,
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
        modifier = modifier
    ) {
        Column {
            WordCardHead(category = category, bookmarked = bookmarked)
            Spacer(Modifier.height(5.dp))
            ProvideTextStyle(MaterialTheme.typography.headlineLarge) {
                Text(text = wordSource.wordName)
            }
        }
    }
}

@Composable
private fun WordCardHead(
    category : String,
    bookmarked: Boolean
) {
    val tint = LocalTintTheme.current.iconTint
    Row(
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        ProvideTextStyle(MaterialTheme.typography.headlineMedium) {
            Text(text = category)
        }
        Icon(
            imageVector = AIForumIcon.Bookmarks,
            contentDescription = "bookmark",
            tint = if (bookmarked) {
                if (tint == Color.Unspecified) {
                    MaterialTheme.colorScheme.onPrimary
                }else {
                    tint
                }
            }else {
                Color.Transparent
            }
        )
    }
}