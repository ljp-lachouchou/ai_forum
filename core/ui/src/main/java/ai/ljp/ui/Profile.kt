package ai.ljp.ui

import ai.ljp.designsystem.component.AIForumLoadingWheel
import ai.ljp.designsystem.component.DynamicAsyncImage
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.lazy.LazyListScope
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ProvideTextStyle
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.paging.compose.LazyPagingItems
import androidx.paging.compose.itemKey
import com.ljp.model.Profile
import com.ljp.model.WordCommentsResource


sealed interface ProfileUiState {
    data class Success(val profile : Profile) : ProfileUiState
    data object Loading : ProfileUiState
    data object Error : ProfileUiState
}

@Composable
fun ProfileCard(
    profileUiState: ProfileUiState,
    modifier: Modifier = Modifier,
    dotContent : (@Composable () -> Unit)? = null,
    onDotClick : () -> Unit = {},
) {
    val isLoading = profileUiState is ProfileUiState.Loading
    Card(
        modifier = modifier
            .fillMaxWidth()
            .padding(16.dp),
        shape = RoundedCornerShape(20.dp),
    ) {
        when(profileUiState) {
            is ProfileUiState.Success -> {
                val profile = profileUiState.profile
                Box(modifier = Modifier.fillMaxWidth()) {
                    Column(
                        modifier = Modifier
                            .align(Alignment.Center)
                    ) {
                        Spacer(Modifier.height(10.dp))
                        Box(modifier = Modifier.wrapContentSize()) {
                            AvatarArea(avatarUrl = profile.avatarUrl)
                            if (dotContent != null) {
                                Box(
                                    modifier = Modifier
                                        .align(Alignment.BottomEnd)
                                        .clickable {
                                            onDotClick()
                                        }
                                ) {
                                    dotContent()
                                }
                            }

                        }
                        ProvideTextStyle(MaterialTheme.typography.headlineMedium) {
                            Text(text = profile.userName)
                        }
                        ProvideTextStyle(MaterialTheme.typography.bodySmall) {
                            Text(text = profile.bio ?:"")
                        }
                    }
                }
            }
            is ProfileUiState.Error -> {}
            else -> Unit
        }
        AnimatedVisibility(visible = isLoading) {
            Box(
                modifier = Modifier.fillMaxWidth()
            ) {
                AIForumLoadingWheel(Modifier.align(Alignment.Center))
            }
        }
    }

}

@Composable
private fun AvatarArea(
    avatarUrl : String?,
) {
    Surface(
        color = Color.White,
        shape = CircleShape,
        modifier = Modifier.padding(8.dp)
    ) {
        DynamicAsyncImage(imageUrl = avatarUrl, contentDescription = null)
    }
}
@Composable
fun Dot(
    color: Color,
    modifier: Modifier = Modifier
) {
    Canvas(
        modifier = modifier.fillMaxSize(),
        onDraw = {
            drawCircle(color = color,
                radius = size.minDimension / 8.0f)
        }
    )
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