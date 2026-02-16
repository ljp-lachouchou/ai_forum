package ai.ljp.designsystem.component.markdown.render.tool

import ai.ljp.designsystem.component.DynamicAsyncImage
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material3.Card
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.text.TextLayoutResult
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.unit.times

@Composable
fun MarkdownBlockItem(
    index: Int,
    block: EditorBlock,
    manager: MarkdownEditorManager,
    vt: VisualTransformation,
    focusRequester: FocusRequester,
) {
    when(block) {
        is EditorBlock.Text -> {
            MarkdownTextBlock(
                index = index,
                block = block,
                manager = manager,
                vt = vt,
                focusRequester = focusRequester
            )
        }
        is EditorBlock.Image -> {
            Card(
                modifier = Modifier.padding(vertical = 8.dp),
                shape = RoundedCornerShape(12.dp)
            ) {
                DynamicAsyncImage(
                    imageUrl = block.url,
                    contentDescription = null,
                    modifier = Modifier.fillMaxWidth().heightIn(max = 300.dp),
                )
            }
        }
    }
}
@Composable
private fun MarkdownTextBlock(
    index: Int,
    block: EditorBlock.Text,
    manager: MarkdownEditorManager,
    vt: VisualTransformation,
    focusRequester: FocusRequester
) {

    BasicTextField(
        value = block.content,
        onValueChange = { manager.updateBlockContent(index, it) },
        visualTransformation = vt,
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp)
            .focusRequester(focusRequester)
            .onFocusChanged { if (it.isFocused) manager.focusedIndex = index },
        textStyle = MaterialTheme.typography.bodyLarge.copy(
            color = MaterialTheme.colorScheme.onSurface,
            lineHeight = 30.sp
        )
    )
}

/**
 * 绘制引用竖线，支持传入 startX 偏移
 */
private fun DrawScope.drawQuoteBar(
    layout: TextLayoutResult,
    offset: Int,
    startX: Float,
    width: Float,
    color: Color
) {
    val lineIndex = layout.getLineForOffset(offset)
    val top = layout.getLineTop(lineIndex)
    val bottom = layout.getLineBottom(lineIndex)

    drawRoundRect(
        color = color,
        topLeft = Offset(startX, top + 2.dp.toPx()),
        size = Size(width, (bottom - top) - 4.dp.toPx()),
        cornerRadius = CornerRadius(2.dp.toPx())
    )
}

/**
 * 绘制列表圆点，支持传入 startX 偏移
 */
private fun DrawScope.drawListDot(
    layout: TextLayoutResult,
    offset: Int,
    startX: Float,
    radius: Float,
    color: Color
) {
    val lineIndex = layout.getLineForOffset(offset)
    val lineTop = layout.getLineTop(lineIndex)
    val lineBottom = layout.getLineBottom(lineIndex)

    // Y 轴中心对齐文字基线感官中心
    val centerY = (lineTop + lineBottom) / 2f

    drawCircle(
        color = color,
        radius = radius,
        // 圆心 X 坐标 = 起始偏移 + 居中修正
        center = Offset(startX + radius, centerY)
    )
}