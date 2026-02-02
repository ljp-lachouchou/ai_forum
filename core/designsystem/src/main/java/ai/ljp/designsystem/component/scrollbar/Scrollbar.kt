package ai.ljp.designsystem.component.scrollbar

import androidx.compose.foundation.gestures.Orientation
import androidx.compose.foundation.gestures.Orientation.Vertical
import androidx.compose.foundation.gestures.detectHorizontalDragGestures
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.gestures.detectVerticalDragGestures
import androidx.compose.foundation.interaction.DragInteraction
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.PressInteraction
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.runtime.Composable
import androidx.compose.runtime.Immutable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableLongStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.runtime.snapshotFlow
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.input.pointer.PointerInputChange
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.layout.Layout
import androidx.compose.ui.layout.Measurable
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.layout.positionInRoot
import androidx.compose.ui.unit.Constraints
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.IntSize
import androidx.compose.ui.unit.dp
import androidx.compose.ui.util.packFloats
import androidx.compose.ui.util.unpackFloat1
import androidx.compose.ui.util.unpackFloat2
import kotlinx.coroutines.TimeoutCancellationException
import kotlinx.coroutines.delay
import kotlinx.coroutines.withTimeout
import kotlin.math.max
import kotlin.math.min
import kotlin.math.roundToInt
private const val SCROLLBAR_PRESS_DELTA_PCT = 0.02f
private const val SCROLLBAR_PRESS_DELAY_MS = 10L
class ScrollbarState {
    private var packageValue by mutableLongStateOf(0L)
    internal fun onScroll(stateValue: ScrollbarStateValue) {
        packageValue = stateValue.packageValue
    }
    val thumbSizePercent
        get() = unpackFloat1(packageValue)
    val thumbMovedPercent
        get() = unpackFloat2(packageValue)

    val thumbTrackSizePercent
        get() = 1f - thumbSizePercent
}
//记录滚动track的属性
@Immutable
@JvmInline
internal value class ScrollbarTrack(
    val packageValue : Long
) {
    constructor(start : Float,end : Float) // 偏移
            :
            this(packFloats(start,end)) // end - start = thumb.size
}
private val ScrollbarTrack.size
    get() = unpackFloat2(packageValue) - unpackFloat1(packageValue)
@Immutable
@JvmInline
value class ScrollbarStateValue internal constructor(
    internal val packageValue : Long
)
fun scrollbarStateValue(
    thumbSizePercent: Float,
    thumbMovedPercent: Float,
) = ScrollbarStateValue(
    packFloats(
        val1 = thumbSizePercent,
        val2 = thumbMovedPercent,
    ),
)
internal fun Orientation.valueOf(offset: Offset) = when(this) {
    Orientation.Vertical -> offset.y
    Orientation.Horizontal -> offset.x
}
internal fun Orientation.valueOf(intOffset: IntOffset) = when (this) {
    Orientation.Horizontal -> intOffset.x
    Orientation.Vertical -> intOffset.y
}
internal fun Orientation.valueOf(intSize : IntSize) = when(this) {
    Orientation.Horizontal -> intSize.width
    Orientation.Vertical -> intSize.height
}
private fun ScrollbarTrack.thumbMovedPercent(
    destination : Float
) = max(
    a = min(
        a = destination / size,
        b = 1f,
    ),
    b = 0f,
)
@Composable
fun Scrollbar(
    orientation: Orientation,
    state : ScrollbarState,
    modifier : Modifier = Modifier,
    minThumbSize: Dp = 40.dp,
    interactionSource: MutableInteractionSource? = null,
    onThumbMoved: (Float) -> Unit,
    thumb : @Composable () -> Unit
) {
    var track by remember { mutableStateOf(ScrollbarTrack(packageValue = 0)) }
    var pressedOffset by remember { mutableStateOf(Offset.Unspecified) }
    var draggedOffset by remember { mutableStateOf(Offset.Unspecified) }
    var interactionThumbMovedPercent by remember { mutableFloatStateOf(Float.NaN) }
    Box(
        modifier = modifier
            .run {
                when(orientation) {
                    Orientation.Vertical -> fillMaxHeight()
                    Orientation.Horizontal -> fillMaxWidth()
                }
            }
            .onGloballyPositioned { layoutCoordinates -> //屏幕信息
                //positionInRoot 避免由于父布局变动导致的点击错位
                val scrollbarStateCoordinates =
                    orientation.valueOf(layoutCoordinates.positionInRoot())
                track = ScrollbarTrack(
                    scrollbarStateCoordinates,
                    scrollbarStateCoordinates + orientation.valueOf(layoutCoordinates.size)
                    )
            }
            .pointerInput(Unit) {
                detectTapGestures (
                    onPress = { offset ->
                        try {
                            withTimeout(viewConfiguration.longPressTimeoutMillis) {
                                tryAwaitRelease()
                            }
                        }catch (exception : TimeoutCancellationException) {
                            val initInteraction = PressInteraction.Press(offset)
                            interactionSource?.tryEmit(initInteraction)
                            pressedOffset = offset
                            interactionSource?.tryEmit(
                                when {
                                    tryAwaitRelease() -> PressInteraction.Release(initInteraction)
                                    else -> PressInteraction.Cancel(initInteraction)
                                }
                            )
                            pressedOffset = Offset.Unspecified
                        }
                    }
                )
            }
            .pointerInput(Unit) {
                var dragInteraction : DragInteraction.Start? = null
                val onDragStart : (Offset) -> Unit = { offset->
                    val start = DragInteraction.Start()
                    dragInteraction = start
                    interactionSource?.tryEmit(start)
                    draggedOffset = offset
                }
                val onDragEnd : () -> Unit = {
                    dragInteraction?.let {
                        interactionSource?.tryEmit(DragInteraction.Stop(it))
                    }
                    draggedOffset = Offset.Unspecified
                }
                val onDragCancel: () -> Unit = {
                    dragInteraction?.let { interactionSource?.tryEmit(DragInteraction.Cancel(it)) }
                    draggedOffset = Offset.Unspecified
                }
                val onDrag : (change: PointerInputChange, dragAmount: Float) -> Unit =
                    onDrag@{_,delta ->
                        if (draggedOffset == Offset.Unspecified) return@onDrag
                        draggedOffset =
                            when(orientation) {
                                Orientation.Horizontal ->
                                    draggedOffset.copy(
                                        x = draggedOffset.x + delta
                                    )
                                Orientation.Vertical ->
                                    draggedOffset.copy(
                                        y = draggedOffset.y + delta
                                    )
                            }
                    }
                when(orientation) {
                    Orientation.Horizontal -> detectHorizontalDragGestures(
                        onDragCancel = onDragCancel,
                        onDragEnd = onDragEnd,
                        onDragStart = onDragStart,
                        onHorizontalDrag = onDrag
                    )
                    Orientation.Vertical -> detectVerticalDragGestures(
                        onDragCancel = onDragCancel,
                        onDragEnd = onDragEnd,
                        onDragStart = onDragStart,
                        onVerticalDrag = onDrag
                    )

                }
            }
    ) {
        //需要动态计算thumbSize和他的一些滑动参数
        Layout(content = thumb) { measurables :  List<Measurable>,constraints : Constraints  ->
            //Constraints 父布局强制要求子布局必须遵守的尺寸范围
            val measurable = measurables.first() // thumb组件
            val thumbSizePx = max(
                state.thumbSizePercent * track.size,
                minThumbSize.toPx()
            )
            val trackSizePx = when(state.thumbTrackSizePercent) {
                0f -> track.size
                else -> (track.size - thumbSizePx) / state.thumbTrackSizePercent
            }
            val thumbMovedPercent = max(
                0f,
                min(
                    state.thumbTrackSizePercent,
                    when {
                        interactionThumbMovedPercent.isNaN() ->state.thumbMovedPercent
                        else -> interactionThumbMovedPercent
                    }
                )

            )
            val thumbMovedPx = thumbMovedPercent *trackSizePx
            val y = when(orientation) {
                Orientation.Horizontal -> 0
                Orientation.Vertical ->thumbMovedPx.roundToInt()
            }
            val x = when(orientation) {
                Orientation.Horizontal -> thumbMovedPx.roundToInt()
                Orientation.Vertical -> 0
            }
            val updatedConstraints =when(orientation) {
                Orientation.Horizontal -> constraints.copy(
                    minWidth = thumbSizePx.roundToInt(),
                    maxWidth = thumbSizePx.roundToInt(),
                )
                Orientation.Vertical -> {
                    constraints.copy(
                        minHeight = thumbSizePx.roundToInt(),
                        maxHeight = thumbSizePx.roundToInt(),
                    )
                }
            } // 设置此layout布局的size
            val placeable = measurable.measure(updatedConstraints)
            layout(placeable.width,placeable.height) {
                placeable.place(x,y)
            // 把layout放置在相对于 Box(父组件) 左上角的 x, y 位置上
            }
        }

    }
    LaunchedEffect(Unit) {
        snapshotFlow { pressedOffset }.collect {pressedOffset->
            if (pressedOffset == Offset.Unspecified) {
                interactionThumbMovedPercent = Float.NaN
                return@collect
            }
            var currentThumbTravelPercent = state.thumbMovedPercent
            val destinationThumbMovedPercent = track.thumbMovedPercent(
                destination = orientation.valueOf(pressedOffset)
            )
            val isPositive = currentThumbTravelPercent < destinationThumbMovedPercent // 正的
            val delta = SCROLLBAR_PRESS_DELTA_PCT * if (isPositive) 1f else -1f
            while (currentThumbTravelPercent != destinationThumbMovedPercent) {
                currentThumbTravelPercent = when {
                    isPositive -> min(
                        currentThumbTravelPercent + delta,
                        destinationThumbMovedPercent
                    )
                    else -> max(
                        currentThumbTravelPercent + delta,
                        destinationThumbMovedPercent
                    )
                }
                onThumbMoved(currentThumbTravelPercent) // 滚动项目移动到哪里
                interactionThumbMovedPercent = currentThumbTravelPercent
                delay(SCROLLBAR_PRESS_DELAY_MS)
            }
        }
    }
    LaunchedEffect(Unit) {
        snapshotFlow { draggedOffset }.collect { draggedOffset ->
            if (draggedOffset == Offset.Unspecified) {
                interactionThumbMovedPercent = Float.NaN
                return@collect
            }
            val currentTravelPercent = track.thumbMovedPercent(
                destination = orientation.valueOf(draggedOffset)
            )
            onThumbMoved(currentTravelPercent)
            interactionThumbMovedPercent = currentTravelPercent
        }
    }
}