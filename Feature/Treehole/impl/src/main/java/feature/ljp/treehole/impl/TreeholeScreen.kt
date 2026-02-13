package feature.ljp.treehole.impl

import ai.ljp.designsystem.component.AIForumLoadingWheel
import ai.ljp.designsystem.component.DynamicAsyncImage
import ai.ljp.designsystem.component.scrollbar.DraggableScrollbar
import ai.ljp.designsystem.component.scrollbar.rememberDraggableScroller
import ai.ljp.designsystem.component.scrollbar.scrollbarState
import ai.ljp.designsystem.theme.Amber100
import ai.ljp.designsystem.theme.Blue100
import ai.ljp.designsystem.theme.Purple100
import ai.ljp.designsystem.theme.Slate600
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.Orientation
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListScope
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ListItem
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ProvideTextStyle
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.paging.compose.LazyPagingItems
import androidx.paging.compose.collectAsLazyPagingItems
import androidx.paging.compose.itemKey
import com.ljp.model.Profile
import com.ljp.model.TreeholeProfileSource
import com.ljp.model.anonymousName
import kotlinx.datetime.Instant
@Composable
internal fun TreeholeScreen(
    modifier: Modifier = Modifier,
    viewModel: TreeholeViewModel = hiltViewModel()
) {
    val treeholeFeedUiState by viewModel.treeholeFeedUiState.collectAsStateWithLifecycle()
    TreeholeScreen(
        treeholeFeedUiState = treeholeFeedUiState,
        modifier = modifier
    )
}

@Composable
internal fun TreeholeScreen(
    treeholeFeedUiState: TreeholeFeedUiState,
    modifier: Modifier = Modifier
) {
    val state = rememberLazyListState()
    val isLoading = treeholeFeedUiState is TreeholeFeedUiState.Loading
    val pageLazyItems = when(treeholeFeedUiState) {
        is TreeholeFeedUiState.Success -> treeholeFeedUiState.feed.collectAsLazyPagingItems()
        else -> null
    }
    val scrollState = state.scrollbarState(pageLazyItems?.itemCount ?: 0)
    val scroller = state.rememberDraggableScroller(pageLazyItems?.itemCount ?: 0)
    Box(
        modifier = Modifier.fillMaxSize()
    ) {
        LazyColumn(
            state = state,
            modifier = modifier.fillMaxSize().padding(horizontal = 24.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            if (pageLazyItems != null) {
                treeholeFeed(pageLazyItems)
            }
        }
        AnimatedVisibility(isLoading) {
            Box(
                modifier = Modifier.fillMaxWidth()
            ) {
                AIForumLoadingWheel(Modifier.align(Alignment.Center))

            }
        }
        state.DraggableScrollbar(
            orientation = Orientation.Vertical,
            state = scrollState,
            onThumbMoved = scroller,
            modifier = Modifier.align(Alignment.CenterEnd)
        )
    }
}
private fun LazyListScope.treeholeFeed(
    pageItems : LazyPagingItems<TreeholeProfileSource>,
) {
    items(pageItems.itemCount, key = pageItems.itemKey { it.id }) {index->
        val treehole = pageItems[index]
        if (treehole != null) {
            TreeholeItem(
                content =treehole.content,
                mood =treehole.mood,
                anonymous =treehole.anonymous,
                createdAt =treehole.createdAt,
                author = treehole.author
            )
        }

    }
}
enum class Mood(val moodName : String,val color : Color) {
    Normal(moodName ="平常", color = Blue100),
    Happy(moodName ="开心", color = Amber100),
    Sad(moodName ="伤心", color = Slate600),
    Anxiety(moodName ="焦虑", color = Purple100)
}
@Composable
private fun TreeholeItem(
    content : String,
    mood : String,
    anonymous : Boolean,
    createdAt : Instant,
    author : Profile
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(
            containerColor = when(mood) {
                "开心" -> {
                    Mood.Happy.color
                }
                "伤心"-> {
                    Mood.Sad.color
                }
                "焦虑" -> {
                    Mood.Anxiety.color
                }
                "平常" -> {
                    Mood.Normal.color
                }
                else -> {
                    Mood.Normal.color
                }
            }
        ),
        border = BorderStroke(1.dp, color = MaterialTheme.colorScheme.onPrimaryContainer)
    ) {
        Column(
            modifier = Modifier.fillMaxWidth(),
            verticalArrangement = Arrangement.spacedBy(8.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            ListItem(
                headlineContent = {
                    ProvideTextStyle(MaterialTheme.typography.bodyMedium) {
                        if (anonymous) {
                            Text(text = "匿名#${ author.anonymousName }")
                        }else {
                            Text(text = author.userName)
                        }
                    }
                },
                supportingContent = {
                    ProvideTextStyle(MaterialTheme.typography.bodySmall) {
                        Text(text = createdAt.toString())
                    }
                },
                leadingContent = {
                    if (anonymous) {
                        Box(
                            modifier = Modifier
                                .background(
                                    color = MaterialTheme.colorScheme.surface,
                                    shape = CircleShape
                                )
                        )
                    }else {
                        DynamicAsyncImage(imageUrl = author.avatarUrl,contentDescription = null)
                    }
                },
                trailingContent = {
                    ProvideTextStyle(MaterialTheme.typography.bodyMedium) {
                        Text(text = mood)
                    }
                }
            )
            ProvideTextStyle(MaterialTheme.typography.bodyMedium) {
                Text(text = content)
            }
        }
    }
}