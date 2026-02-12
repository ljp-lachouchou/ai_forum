package feature.ljp.treehole.impl

import ai.ljp.designsystem.component.AIForumLoadingWheel
import ai.ljp.designsystem.component.scrollbar.DraggableScrollbar
import ai.ljp.designsystem.component.scrollbar.rememberDraggableScroller
import ai.ljp.designsystem.component.scrollbar.scrollbarState
import ai.ljp.network.model.TreeholeItem
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.gestures.Orientation
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListScope
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.key
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.paging.compose.LazyPagingItems
import androidx.paging.compose.collectAsLazyPagingItems
import androidx.paging.compose.itemKey
import com.ljp.model.Treehole

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
    pageItems : LazyPagingItems<Treehole>,
) {
    items(pageItems.itemCount, key = pageItems.itemKey { it.id }) {index->
        val treehole = pageItems[index]
        if (treehole != null) {

        }

    }
}
@Composable
private fun TreeholeItem() {

}