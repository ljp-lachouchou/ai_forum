package ai.ljp.designsystem.component.scrollbar

import androidx.compose.foundation.gestures.ScrollableState
import androidx.compose.ui.geometry.Offset
import kotlin.math.abs

//连续性的第一个item的index

internal inline fun<LazyState : ScrollableState,LazyItem> LazyState.interpolateFirstItemIndex(
    visibleItems : List<LazyItem>,
    crossinline itemSize : LazyState.(LazyItem) -> Int,
    crossinline offset : LazyState.(LazyItem) -> Int,//动态物理状态 需要LazyState约束
    crossinline nextItemOnMainAxis :LazyState.(LazyItem) -> LazyItem?,
    crossinline itemIndex : (LazyItem) -> Int
) : Float{
    if (visibleItems.isEmpty()) return 0f
    val firstItem = visibleItems.first()
    val firstItemIndex = itemIndex(firstItem)
    if (firstItemIndex < 0) return Float.NaN // 列表正在执行“重组”或数据清空
    val firstOffset = offset(firstItem).toFloat()
    val firstItemSize = itemSize(firstItem)
    if (firstItemSize == 0) return Float.NaN
    val offsetPercentage : Float = abs(firstOffset) / firstItemSize
    val nextItem = nextItemOnMainAxis(firstItem) ?: return firstItemIndex + offsetPercentage
    val nextItemIndex = itemIndex(nextItem)
    return firstItemIndex + ((nextItemIndex - firstItemIndex) * offsetPercentage) //应对“索引跳跃”

}
internal fun itemVisibilityPercentage(
    itemSize : Int,
    itemStartOffset : Int,
    viewportStartOffset : Int,
    viewportEndOffset : Int
) : Float {
    if (itemSize == 0) return 0f
    val itemEnd = itemSize + itemStartOffset
    val startOffset = when {
        itemStartOffset > viewportStartOffset -> 0 // 还没到顶，没被裁切 item没出去视图范围
        else -> abs(abs(viewportStartOffset) - abs(itemStartOffset)) // item出去视图范围 露出去的部分
    }
    val endOffset = when {
        itemEnd < viewportEndOffset -> 0 // 还没到底，没被裁切
        else -> abs(abs(itemEnd) - abs(viewportEndOffset)) // 露出去的部分
    }
    val size = itemSize.toFloat()
    return (size - startOffset - endOffset) / size
}