package ai.ljp.designsystem.component.scrollbar

import android.util.Size
import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.SpringSpec
import androidx.compose.animation.core.spring
import androidx.compose.foundation.gestures.Orientation
import androidx.compose.foundation.gestures.ScrollableState
import androidx.compose.foundation.interaction.InteractionSource
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.collectIsDraggedAsState
import androidx.compose.foundation.interaction.collectIsPressedAsState
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.State
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorProducer
import androidx.compose.ui.graphics.Outline
import androidx.compose.ui.graphics.drawOutline
import androidx.compose.ui.graphics.drawscope.ContentDrawScope
import androidx.compose.ui.node.DrawModifierNode
import androidx.compose.ui.node.ModifierNodeElement
import androidx.compose.ui.node.invalidateDraw
import androidx.compose.ui.unit.LayoutDirection
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.delay

private const val SCROLLBAR_INACTIVE_TO_DORMANT_TIME_IN_MS = 300L
@Composable
fun ScrollableState.DraggableScrollbar(
    orientation: Orientation,
    state : ScrollbarState,
    onThumbMoved: (Float) -> Unit,
    modifier: Modifier = Modifier
) {
    val interactionSource by remember { mutableStateOf(MutableInteractionSource()) }
    Scrollbar(
        orientation = orientation,
        state = state,
        modifier = modifier,
        interactionSource = interactionSource,
        onThumbMoved = onThumbMoved,
        thumb = {
            DraggableThumb(interactionSource,orientation)
        },
    )
}
@Composable
private fun Modifier.scrollbarThumb(interactionSource: InteractionSource,scrollableState: ScrollableState) : Modifier {
    val colorState = thumbColor(interactionSource,scrollableState)
    return this then ScrollbarThumbElement { colorState.value }
}
@Composable
private fun ScrollableState.DraggableThumb(
    interactionSource: InteractionSource,
    orientation: Orientation
) {
    Box(
        modifier = Modifier
            .run {
                when(orientation) {
                    Orientation.Horizontal -> height(12.dp).fillMaxWidth()
                    Orientation.Vertical -> width(12.dp).fillMaxHeight()
                }
            }.scrollbarThumb(interactionSource,this)
    )
}


private data class ScrollbarThumbElement(
    val colorProducer : ColorProducer
) : ModifierNodeElement<ScrollbarThumbNode>() {
    override fun create(): ScrollbarThumbNode  = ScrollbarThumbNode(colorProducer)

    override fun update(node: ScrollbarThumbNode) {
        node.colorProducer = colorProducer
        node.invalidateDraw() // 重新绘图
    }

}

private class ScrollbarThumbNode(
    var colorProducer: ColorProducer
) : Modifier.Node(), DrawModifierNode {
    private val shape = RoundedCornerShape(16.dp)
    private var lastSize : androidx.compose.ui.geometry.Size? = null
    private var lastLayoutDirection : LayoutDirection? = null
    private var lastOutline: Outline? = null
    override fun ContentDrawScope.draw() {
        val color = colorProducer()
        val outline = if (size == lastSize && lastLayoutDirection == layoutDirection) {
            lastOutline!!
        }else {
            shape.createOutline(size,layoutDirection,this)
        }
        if (color != Color.Unspecified) drawOutline(outline = outline,color = color)
        lastSize = size
        lastLayoutDirection = layoutDirection
        lastOutline = outline

    }

}
@Composable
private fun thumbColor(
    interactionSource: InteractionSource,
    scrollableState: ScrollableState
) : State<Color> {
    var state by remember { mutableStateOf(ThumbState.Dormant) }
    val pressed by interactionSource.collectIsPressedAsState()
    val dragged by interactionSource.collectIsDraggedAsState()
    val active =(scrollableState.canScrollForward || scrollableState.canScrollBackward) &&
            (pressed || dragged || scrollableState.isScrollInProgress) // 被按压、拖拽或者就在移动过程中
    val color = animateColorAsState(
        targetValue = when(state) {
            ThumbState.Activate -> MaterialTheme.colorScheme.onSurface.copy(0.5f)
            ThumbState.Inactivate -> MaterialTheme.colorScheme.onSurface.copy(alpha = 0.2f)
            ThumbState.Dormant -> Color.Transparent
        },
        animationSpec = SpringSpec(
            stiffness = Spring.StiffnessLow,

        ),
        label = "Scrollbar thumb color",
    )
    LaunchedEffect(active) {
        when (active) {
            true -> state = ThumbState.Activate
            false -> if (state == ThumbState.Activate) {
                state = ThumbState.Inactivate
                delay(SCROLLBAR_INACTIVE_TO_DORMANT_TIME_IN_MS)
                state = ThumbState.Dormant
            }
        }
    }
    return color
}
enum class ThumbState {
    Activate,
    Inactivate,
    Dormant
}