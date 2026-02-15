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
    // 关键状态：存储文本布局结果，用于 Canvas 定位
    var textLayoutResult by remember { mutableStateOf<TextLayoutResult?>(null) }
    val barWidth = 4.dp
    val barSpacing = 2.dp
    val quoteColor = MaterialTheme.colorScheme.primary
    BasicTextField(
        value = block.content,
        onValueChange = { manager.updateBlockContent(index, it) },
        visualTransformation = vt,
        onTextLayout = { textLayoutResult = it }, // 获取布局信息
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp)
            .focusRequester(focusRequester)
            .onFocusChanged { if (it.isFocused) manager.focusedIndex = index },
        textStyle = MaterialTheme.typography.bodyLarge.copy(
            color = MaterialTheme.colorScheme.onSurface,
            lineHeight = 24.sp // 固定行高有助于视觉对齐
        ),
        decorationBox = { innerTextField ->
            Box(modifier = Modifier.fillMaxWidth()) {
                // 1. 绘制层：根据每一行的内容决定是否画线
                Canvas(modifier = Modifier.matchParentSize()) {
                    val layout = textLayoutResult ?: return@Canvas
                    val rawText = block.content.text
                    val lines = rawText.split("\n")

                    var currentLineStartOffset = 0
                    lines.forEachIndexed { i, lineContent ->
                        // 检查当前行是否以引用符开头
                        when {
                            lineContent.startsWith("> ") -> {
                                quote(
                                    layout = layout,
                                    currentLineStartOffset =
                                        currentLineStartOffset,
                                    lineContent = lineContent,
                                    quoteColor = quoteColor
                                )
                            }
                        }
                        currentLineStartOffset += lineContent.length + 1 // +1 是换行符
                    }
                }

                // 2. 输入层：为了给左侧竖线留出空间，动态增加 Padding
                // 如果整块没有任何引用，就不留间距；如果有，则留出竖线宽度+间距
                val hasAnyQuote = remember(block.content.text) {
                    block.content.text.lines().any { it.startsWith("> ") }
                }

                Box(modifier = Modifier.padding(start = if (hasAnyQuote) barWidth + barSpacing else 0.dp)) {
                    innerTextField()
                }
            }
        }
    )
}
private fun DrawScope.quote(
    layout : TextLayoutResult,
    lineContent : String,
    currentLineStartOffset : Int,
    barWidth : Dp= 4.dp,
    quoteColor : Color,
) {
    val lineIndex = layout.getLineForOffset(currentLineStartOffset)
    val lastLineIndex = layout.getLineForOffset(currentLineStartOffset + lineContent.length)

    val top = layout.getLineTop(lineIndex)
    val bottom = layout.getLineBottom(lastLineIndex)

    drawRoundRect(
        color = quoteColor,
        topLeft = Offset(0f, top + 2.dp.toPx()), // 微调顶部间距
        size = Size(barWidth.toPx(), (bottom - top) - 4.dp.toPx()), // 微调高度
        cornerRadius = CornerRadius(2.dp.toPx())
    )
}

