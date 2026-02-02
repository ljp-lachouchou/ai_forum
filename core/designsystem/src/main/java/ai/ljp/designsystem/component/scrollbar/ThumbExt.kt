package ai.ljp.designsystem.component.scrollbar

import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.grid.LazyGridState
import androidx.compose.foundation.lazy.staggeredgrid.LazyStaggeredGridState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import kotlin.math.roundToInt
@Composable
fun LazyListState.rememberDraggableScroller(
    itemsCount: Int,
): (Float) -> Unit =
    rememberDraggableScroller(
        itemCount = itemsCount,
        scroll = ::scrollToItem,
    )
@Composable
fun LazyGridState.rememberDraggableScroller(
    itemsCount: Int,
): (Float) -> Unit =
    rememberDraggableScroller(
        itemCount = itemsCount,
        scroll = ::scrollToItem,
    )
@Composable
fun LazyStaggeredGridState.rememberDraggableScroller(
    itemsCount: Int,
): (Float) -> Unit =
    rememberDraggableScroller(
        itemCount = itemsCount,
        scroll = ::scrollToItem,
    )
@Composable
private inline fun rememberDraggableScroller(
    itemCount : Int,
    crossinline scroll : suspend (index : Int) -> Unit
) : (Float) -> Unit {
    var percentage by remember { mutableFloatStateOf(Float.NaN) }
    LaunchedEffect(percentage) {
        if (percentage.isNaN()) return@LaunchedEffect
        val indexToFind = (itemCount * percentage).roundToInt()
        scroll(indexToFind)
    }
    return {
        newPercentage -> percentage = newPercentage
    }
}