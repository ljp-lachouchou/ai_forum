package ai.ljp.designsystem.component.markdown.render.tool

import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.TextRange
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.unit.dp

/**
 *需换行的
 */
interface MarkdownStrategy {
    fun apply(current : TextFieldValue) : TextFieldValue
}

sealed interface MarkdownStyle : MarkdownStrategy {
    data class Inline(val tag : String) : MarkdownStyle {
        override fun apply(current: TextFieldValue): TextFieldValue {
            val text = current.text
            val sel = current.selection
            val selectedText = text.substring(sel.start, sel.end)

            val newText = text.replaceRange(sel.start, sel.end, "$tag$selectedText$tag")
            // 光标逻辑：如果有选中，包裹选中；没选中，光标移到中间
            val newSelection = if (sel.collapsed) {
                TextRange(sel.start + tag.length)
            } else {
                TextRange(sel.start + tag.length, sel.end + tag.length)
            }
            return current.copy(text = newText, selection = newSelection)
        }

    }
    data class Block(val prefix : String) : MarkdownStyle {
        override fun apply(current: TextFieldValue): TextFieldValue {
            val text = current.text
            val sel = current.selection
            val lineStart = text.lastIndexOf('\n',sel.start - 1).let {
                if (it == -1) {
                    0
                }else {
                    it + 1
                }
            }
            val isAtLineStart = sel.start == lineStart
            return if (isAtLineStart) {
                if (text.startsWith(prefix,lineStart)) {
                    current.copy(
                        text = text.removeRange(lineStart,lineStart + prefix.length),
                        selection = TextRange(maxOf(lineStart, sel.start - prefix.length))
                    )
                }else {
                    current.copy(
                        text = text.replaceRange(lineStart,lineStart,prefix),
                        selection = TextRange(sel.start + prefix.length)
                    )
                }
            }else {
                val insertString = "\n$prefix"
                val newText = text.replaceRange(sel.start, sel.start, insertString)
                current.copy(
                    text = newText,
                    selection = TextRange(sel.start + insertString.length)
                )
            }
        }

    }
}
@Composable
fun ToolbarButton(
    imageVector: ImageVector,
    contentDescription: String? = null,
    onClick: () -> Unit
) {
    IconButton(
        onClick = onClick,
        modifier = Modifier
            .size(40.dp) // 调整大小以适应 48.dp 高度的 Toolbar
            .padding(4.dp)
    ) {
        Icon(
            imageVector = imageVector,
            contentDescription = contentDescription,
            tint = MaterialTheme.colorScheme.onSurfaceVariant,
            modifier = Modifier.size(24.dp)
        )
    }
}