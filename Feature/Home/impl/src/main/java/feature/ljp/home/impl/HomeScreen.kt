package feature.ljp.home.impl

import ai.ljp.designsystem.component.AIForumLoadingWheel
import ai.ljp.designsystem.component.scrollbar.DraggableScrollbar
import ai.ljp.designsystem.component.scrollbar.rememberDraggableScroller
import ai.ljp.designsystem.component.scrollbar.scrollbarState
import ai.ljp.ui.WordsFeedUiState
import ai.ljp.ui.wordsFeed
import androidx.activity.compose.ReportDrawnWhen
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.gestures.Orientation
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.systemBars
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.foundation.lazy.staggeredgrid.LazyVerticalStaggeredGrid
import androidx.compose.foundation.lazy.staggeredgrid.StaggeredGridCells
import androidx.compose.foundation.lazy.staggeredgrid.rememberLazyStaggeredGridState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import kotlinx.coroutines.flow.StateFlow
import androidx.paging.compose.collectAsLazyPagingItems

@Composable
fun HomeScreen(
    onProfileClick: (String) -> Unit,
    onPostClick: (String) -> Unit,
    modifier: Modifier = Modifier,
    viewModel: HomeViewModel = hiltViewModel()
) {
    val wordsFeedState by viewModel.feedUiState.collectAsState()
    val isSyncing by viewModel.isSyncing.collectAsState()
    HomeScreen(
        isSyncing = isSyncing,
        wordsFeedState = wordsFeedState,
        onProfileClick = onProfileClick,
        onPostClick = onPostClick,
        onToggleLikeClick = viewModel::toggleLike,
        onToggleBookmarkClick = viewModel::toggleBookmark,
        isLike = viewModel::isLike,
        isBookmark = viewModel::isBookmark,
        modifier = modifier
    )
}
@Composable
internal fun HomeScreen(
    isSyncing : Boolean,
    wordsFeedState : WordsFeedUiState,
    onProfileClick : (String) -> Unit,
    onPostClick : (String) -> Unit,
    onToggleLikeClick : (String) -> Unit,
    onToggleBookmarkClick : (String) -> Unit,
    isLike : (String) -> StateFlow<Boolean>,
    isBookmark : (String) -> StateFlow<Boolean>,
    modifier: Modifier = Modifier
) {
    val isFeedLoading = wordsFeedState is WordsFeedUiState.Loading
    ReportDrawnWhen { !isSyncing &&  !isFeedLoading}
    val itemCount = feedItemSize(wordsFeedState)
    val state = rememberLazyStaggeredGridState()
    val scrollState = state.scrollbarState(
        itemCount = itemCount
    )

    Box(
        modifier = modifier
            .fillMaxSize()
    ) {
        when(wordsFeedState) {
            WordsFeedUiState.Loading -> Unit
            is WordsFeedUiState.Success -> {
                val items = wordsFeedState.feed.collectAsLazyPagingItems()
                val list = items.itemSnapshotList.filterNotNull()
                LazyVerticalStaggeredGrid(
                    columns = StaggeredGridCells.Adaptive(300.dp),
                    state = state,
                    contentPadding = PaddingValues(16.dp),
                    verticalItemSpacing = 24.dp,
                    horizontalArrangement = Arrangement.spacedBy(16.dp),
                ) {
                    wordsFeed(
                        wordsSource = list,
                        onProfileClick = onProfileClick,
                        onPostClick = onPostClick,
                        onToggleLikeClick = onToggleLikeClick,
                        onToggleBookmarkClick = onToggleBookmarkClick,
                        isLike = isLike,
                        isBookmark = isBookmark
                    )
                }
            }
        }

        AnimatedVisibility(
            visible = isSyncing || isFeedLoading,
            enter = slideInVertically(
                initialOffsetY = { fullHeight -> -fullHeight },
            ) + fadeIn(),
            exit = slideOutVertically(
                targetOffsetY = { fullHeight -> -fullHeight },
            ) + fadeOut(),
        ) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(8.dp)
            ) {
                AIForumLoadingWheel(Modifier.align(Alignment.Center))
            }
        }
        state.DraggableScrollbar(
            orientation = Orientation.Vertical,
            state = scrollState,
            onThumbMoved = state.rememberDraggableScroller(
                itemsCount = itemCount
            ),
            modifier = Modifier
                .fillMaxHeight()
                .windowInsetsPadding(WindowInsets.systemBars)
                .align(Alignment.CenterEnd)
        )
    }
}
@Composable
private fun feedItemSize(
    wordsFeedUiState: WordsFeedUiState
) : Int = when(wordsFeedUiState) {
    is WordsFeedUiState.Success ->{
            wordsFeedUiState.feed.collectAsLazyPagingItems().itemCount
        }
    else -> 0
}