package ai.ljp.designsystem.component.markdown.render.tool

import androidx.compose.runtime.Stable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.setValue
import androidx.compose.ui.text.input.TextFieldValue
import java.util.UUID

sealed interface EditorBlock {
    val id: String
    data class Text(var content : TextFieldValue = TextFieldValue(""),
                    override val id: String = UUID.randomUUID().toString()
    ) : EditorBlock
    data class Image(val url : String, override val id: String =
        UUID.randomUUID().toString()
    ) : EditorBlock
    }

@Stable
class MarkdownEditorManager(initialMarkdown : String) {
    val blocks = mutableStateListOf<EditorBlock>().apply {
        addAll(importFromMarkdown(initialMarkdown))
    }
    private fun importFromMarkdown(raw : String) : List<EditorBlock> {
        val nodes = mutableListOf<EditorBlock>()
        val regex = Regex("(!\\[.*?]\\(.*?\\))")
        val parts = raw.split(regex)
        val images = regex.findAll(raw).map { it.value }.toList()
        parts.forEachIndexed { i, text ->
            if (text.isNotEmpty() || (i == 0 && images.isEmpty())) {
                nodes.add(EditorBlock.Text(TextFieldValue(text.trim('\n'))))
            }
            if (i < images.size) {
                val url = images[i].substringAfter("(").substringBefore(")")
                nodes.add(EditorBlock.Image(url))
            }
        }
        return if (nodes.isEmpty()) listOf(EditorBlock.Text()) else nodes
    }
    fun exportMarkdown(): String {
        return blocks.joinToString("\n\n") { block ->
            when (block) {
                is EditorBlock.Text -> block.content.text
                is EditorBlock.Image -> "![image](${block.url})"
            }
        }
    }
    var focusedIndex by mutableIntStateOf(0)

    // 关键：专门的文本更新函数
    fun updateBlockContent(index: Int, newValue: TextFieldValue) {
        if (index in blocks.indices) {
            val currentBlock = blocks[index]
            if (currentBlock is EditorBlock.Text) {
                // 只有内容真的变化时才赋值，防止死循环重组
                if (currentBlock.content != newValue) {
                    blocks[index] = currentBlock.copy(content = newValue)
                }
            }
        }
    }
    fun insertImage(currentIndex: Int, url: String) {
        val currentBlock = blocks[currentIndex] as? EditorBlock.Text ?: return
        val text = currentBlock.content.text
        val sel = currentBlock.content.selection

        // 拆分当前文字块
        val before = text.substring(0, sel.start).trim()
        val after = text.substring(sel.end).trim()

        blocks[currentIndex] = EditorBlock.Text(TextFieldValue(before))
        blocks.add(currentIndex + 1, EditorBlock.Image(url))
        blocks.add(currentIndex + 2, EditorBlock.Text(TextFieldValue(after)))
    }
}