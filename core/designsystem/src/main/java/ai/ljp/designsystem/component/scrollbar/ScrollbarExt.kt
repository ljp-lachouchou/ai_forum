package ai.ljp.designsystem.component.scrollbar

import androidx.compose.foundation.gestures.Orientation
import androidx.compose.foundation.lazy.LazyListItemInfo
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.grid.LazyGridItemInfo
import androidx.compose.foundation.lazy.grid.LazyGridState
import androidx.compose.foundation.lazy.staggeredgrid.LazyStaggeredGridItemInfo
import androidx.compose.foundation.lazy.staggeredgrid.LazyStaggeredGridState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.runtime.snapshotFlow
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.filterNotNull
import kotlin.math.min

@Composable
fun LazyListState.scrollbarState(
    itemCount : Int,
    itemIndex : (LazyListItemInfo) -> Int = LazyListItemInfo::index
) : ScrollbarState {
    val state = remember { ScrollbarState() }
    LaunchedEffect(this,itemCount) {
        snapshotFlow {
            if (itemCount == 0) return@snapshotFlow null
            val visibleItems = layoutInfo.visibleItemsInfo
            if (visibleItems.isEmpty()) return@snapshotFlow null
            val firstIndexFloat = min(
                a = interpolateFirstItemIndex(
                    visibleItems = visibleItems,
                    itemSize = {it.size},
                    offset = {it.offset},
                    nextItemOnMainAxis = {first -> visibleItems.find { it != first }},
                    itemIndex = itemIndex,
                ),
                itemCount.toFloat()
            )
            if (firstIndexFloat.isNaN()) return@snapshotFlow null
            val itemVisibleTotalSize = visibleItems.floatSumOf { itemInfo ->
                itemVisibilityPercentage(
                    itemSize = itemInfo.size,
                    itemStartOffset = itemInfo.offset,
                    viewportStartOffset = layoutInfo.viewportStartOffset,
                    viewportEndOffset = layoutInfo.viewportEndOffset
                )
            }
            val thumbTravelPercent = min(
                1f,
                firstIndexFloat / itemVisibleTotalSize
            )
            val thumbSizePercent = min(
                itemVisibleTotalSize / itemCount,
                1f
            )
            scrollbarStateValue(
                thumbSizePercent = thumbSizePercent,
                thumbMovedPercent = when {
                    layoutInfo.reverseLayout -> 1f - thumbTravelPercent
                    else -> thumbTravelPercent
                },
            )

        }
            .filterNotNull()
            .distinctUntilChanged()
            .collect { state.onScroll(it) }
    }
    return state
}
@Composable
fun LazyGridState.scrollbarState(
    itemCount : Int,
    itemIndex : (LazyGridItemInfo) -> Int = LazyGridItemInfo::index
) : ScrollbarState {
    val state = remember { ScrollbarState() }
    LaunchedEffect(this,itemCount) {
        snapshotFlow {
            if (itemCount == 0) return@snapshotFlow null
            val visibleItemsInfo = layoutInfo.visibleItemsInfo
            if (visibleItemsInfo.isEmpty()) return@snapshotFlow null
            val firstIndexFloat = min(
                a = interpolateFirstItemIndex(
                    visibleItems = visibleItemsInfo,
                    itemSize = { layoutInfo.orientation.valueOf(it.size) },
                    offset = { layoutInfo.orientation.valueOf(it.offset) },
                    nextItemOnMainAxis = {first ->
                        when (layoutInfo.orientation) {
                            Orientation.Vertical -> visibleItemsInfo.find {
                                it != first && it.row != first.row
                            }

                            Orientation.Horizontal -> visibleItemsInfo.find {
                                it != first && it.column != first.column
                            }
                        }
                    },
                    itemIndex = itemIndex,
                ),
                itemCount.toFloat()
            )
            if (firstIndexFloat.isNaN()) return@snapshotFlow null
            val itemVisibleTotalSize = visibleItemsInfo.floatSumOf { itemInfo ->
                itemVisibilityPercentage(
                    itemSize = layoutInfo.orientation.valueOf(itemInfo.size),
                    itemStartOffset = layoutInfo.orientation.valueOf(itemInfo.offset),
                    viewportStartOffset = layoutInfo.viewportStartOffset,
                    viewportEndOffset = layoutInfo.viewportEndOffset
                )
            }
            val thumbTravelPercent = min(
                1f,
                firstIndexFloat / itemVisibleTotalSize
            )
            val thumbSizePercent = min(
                itemVisibleTotalSize / itemCount,
                1f
            )
            scrollbarStateValue(
                thumbSizePercent = thumbSizePercent,
                thumbMovedPercent = when {
                    layoutInfo.reverseLayout -> 1f - thumbTravelPercent
                    else -> thumbTravelPercent
                },
            )

        }
            .filterNotNull()
            .distinctUntilChanged()
            .collect { state.onScroll(it) }
    }
    return state
}
@Composable
fun LazyStaggeredGridState.scrollbarState(
    itemCount : Int,
    itemIndex : (LazyStaggeredGridItemInfo) -> Int = LazyStaggeredGridItemInfo::index
) : ScrollbarState {
    val state = remember { ScrollbarState() }
    LaunchedEffect(this,itemCount) {
        snapshotFlow {
            if (itemCount == 0) return@snapshotFlow null
            val visibleItemsInfo = layoutInfo.visibleItemsInfo
            if (visibleItemsInfo.isEmpty()) return@snapshotFlow null
            val firstIndexFloat = min(
                a = interpolateFirstItemIndex(
                    visibleItems = visibleItemsInfo,
                    itemSize = { layoutInfo.orientation.valueOf(it.size) },
                    offset = { layoutInfo.orientation.valueOf(it.offset) },
                    nextItemOnMainAxis = {first ->
                        visibleItemsInfo.find {
                            it != first && it.lane == first.lane
                        }
                    },
                    itemIndex = itemIndex,
                ),
                itemCount.toFloat()
            )
            if (firstIndexFloat.isNaN()) return@snapshotFlow null
            val itemVisibleTotalSize = visibleItemsInfo.floatSumOf { itemInfo ->
                itemVisibilityPercentage(
                    itemSize = layoutInfo.orientation.valueOf(itemInfo.size),
                    itemStartOffset = layoutInfo.orientation.valueOf(itemInfo.offset),
                    viewportStartOffset = layoutInfo.viewportStartOffset,
                    viewportEndOffset = layoutInfo.viewportEndOffset
                )
            }
            val thumbTravelPercent = min(
                1f,
                firstIndexFloat / itemVisibleTotalSize
            )
            val thumbSizePercent = min(
                itemVisibleTotalSize / itemCount,
                1f
            )
            scrollbarStateValue(
                thumbSizePercent = thumbSizePercent,
                thumbMovedPercent = thumbTravelPercent,
            )

        }
            .filterNotNull()
            .distinctUntilChanged()
            .collect { state.onScroll(it) }
    }
    return state
}
private inline fun <T> List<T>.floatSumOf(selector : (T) -> Float) : Float =
    fold(initial = 0f) { acc,item -> acc + selector(item) }