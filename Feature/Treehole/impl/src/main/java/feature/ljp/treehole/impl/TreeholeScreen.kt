package feature.ljp.treehole.impl

import ai.ljp.designsystem.component.scrollbar.DraggableScrollbar
import ai.ljp.designsystem.component.scrollbar.rememberDraggableScroller
import ai.ljp.designsystem.component.scrollbar.scrollbarState
import androidx.compose.foundation.gestures.Orientation
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.paging.compose.collectAsLazyPagingItems

@Composable
internal fun TreeholeScreen(
    treeholeFeedUiState: TreeholeFeedUiState,
    modifier: Modifier = Modifier
) {
    val state = rememberLazyListState()
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

        }
        state.DraggableScrollbar(
            orientation = Orientation.Vertical,
            state = scrollState,
            onThumbMoved = scroller,
            modifier = Modifier.align(Alignment.CenterEnd)
        )
    }
}