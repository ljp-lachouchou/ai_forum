package feature.ljp.home.impl

import ai.ljp.designsystem.component.AIForumLoadingWheel
import ai.ljp.designsystem.component.expandbutton.ExpandButton
import ai.ljp.designsystem.component.expandbutton.LocalPopupItemSize
import ai.ljp.designsystem.component.expandbutton.PopupItem
import ai.ljp.designsystem.component.expandbutton.PopupItemSize
import ai.ljp.designsystem.component.expandbutton.rememberExpandButtonState
import ai.ljp.designsystem.component.scrollbar.DraggableScrollbar
import ai.ljp.designsystem.component.scrollbar.rememberDraggableScroller
import ai.ljp.designsystem.component.scrollbar.scrollbarState
import ai.ljp.designsystem.icon.AIForumIcon
import ai.ljp.ui.WordsFeedUiState
import ai.ljp.ui.wordsFeed
import android.util.Log
import androidx.activity.compose.ReportDrawnWhen
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.gestures.Orientation
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
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
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import kotlinx.coroutines.flow.StateFlow
import androidx.paging.compose.collectAsLazyPagingItems

@Composable
fun HomeScreen(
    onProfileClick: (String) -> Unit,
    onPostClick: (String) -> Unit,
    modifier: Modifier = Modifier,
    onPostCreateClick : () -> Unit,
    onTreeholeCreateClick : () -> Unit,
    viewModel: HomeViewModel = hiltViewModel()
) {
    val wordsFeedState by viewModel.feedUiState.collectAsStateWithLifecycle()
    val isSyncing by viewModel.isSyncing.collectAsStateWithLifecycle()
    HomeScreen(
        isSyncing = isSyncing,
        wordsFeedState = wordsFeedState,
        onProfileClick = onProfileClick,
        onPostClick = onPostClick,
        onToggleLikeClick = viewModel::toggleLike,
        onToggleBookmarkClick = viewModel::toggleBookmark,
        isLike = viewModel::isLike,
        isBookmark = viewModel::isBookmark,
        modifier = modifier,
        onPostCreateClick = onPostCreateClick,
        onTreeholeCreateClick = onTreeholeCreateClick
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
    onPostCreateClick : () -> Unit,
    onTreeholeCreateClick : () -> Unit,
    modifier: Modifier = Modifier
) {
    val isFeedLoading = wordsFeedState is WordsFeedUiState.Loading
    ReportDrawnWhen { !isSyncing &&  !isFeedLoading}
    val state = rememberLazyStaggeredGridState()
    val items = when(wordsFeedState) {
        is WordsFeedUiState.Success -> {
            wordsFeedState.feed.collectAsLazyPagingItems()
        }
        else -> {
            null
        }
    }
    val scrollState = state.scrollbarState(
        itemCount = items?.itemCount ?: 0
    )
    val expandButtonState = rememberExpandButtonState(false)
    Box(
        modifier = modifier
            .fillMaxSize()
    ) {
        when(wordsFeedState) {
            WordsFeedUiState.Loading -> Unit
            is WordsFeedUiState.Success -> {
                LazyVerticalStaggeredGrid(
                    columns = StaggeredGridCells.Adaptive(300.dp),
                    state = state,
                    contentPadding = PaddingValues(16.dp),
                    verticalItemSpacing = 24.dp,
                    horizontalArrangement = Arrangement.spacedBy(16.dp),
                    modifier = Modifier.fillMaxSize()
                ) {
                    wordsFeed(
                        wordsSource = items!!,
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
        ExpandButton(
            state = expandButtonState,
            modifier = Modifier.align(Alignment.CenterEnd),
            actionIcon = AIForumIcon.Add,
            popupContent = {
                CompositionLocalProvider(
                    LocalPopupItemSize provides PopupItemSize(expandButtonState.size)
                ) {
                    Column(
                        verticalArrangement = Arrangement.spacedBy(4.dp),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        PopupItem(
                            feature.ljp.home.api.R.string.feature_home_api_post_create,
                            AIForumIcon.Post
                        ) {
                            onPostCreateClick()
                        }
                        PopupItem(
                            feature.ljp.home.api.R.string.feature_home_api_treehole_create,
                            AIForumIcon.Treehole
                        ) {
                            onTreeholeCreateClick()
                        }
                    }
                }
            }
        )
        state.DraggableScrollbar(
            orientation = Orientation.Vertical,
            state = scrollState,
            onThumbMoved = state.rememberDraggableScroller(
                itemsCount = items?.itemCount ?: 0
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