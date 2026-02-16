package ai.ljp.designsystem.component.markdown.render.tool

import androidx.compose.runtime.Stable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.setValue
import androidx.compose.ui.text.TextRange
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
                val oldValue = currentBlock.content

                val smartValue = handleSmartBreak(oldValue, newValue)
                val finalValue = smartValue ?: newValue

                if (oldValue != finalValue) {
                    blocks[index] = currentBlock.copy(content = finalValue)
                }
            }
        }
    }
    internal fun handleBackspaceAtStart(currentIndex: Int) {
        if (currentIndex <= 0) return // 第一块，无法向上合并

        val currentBlock = blocks[currentIndex] as? EditorBlock.Text ?: return
        val prevBlock = blocks[currentIndex - 1]

        when (prevBlock) {
            is EditorBlock.Image -> {
                if (currentIndex >= 2 && blocks[currentIndex - 2] is EditorBlock.Image) {
                    blocks.removeAt(currentIndex - 1)
                    focusedIndex = currentIndex - 1
                } else if (currentIndex >= 2 && blocks[currentIndex - 2] is EditorBlock.Text) {
                    val targetTextBlock = blocks[currentIndex - 2] as EditorBlock.Text
                    val combinedText = targetTextBlock.content.text + currentBlock.content.text

                    val newSelection = TextRange(targetTextBlock.content.text.length)
                    blocks[currentIndex - 2] = targetTextBlock.copy(
                        content = TextFieldValue(combinedText, newSelection)
                    )

                    blocks.removeAt(currentIndex)
                    blocks.removeAt(currentIndex - 1)

                    focusedIndex = currentIndex - 2
                } else {
                    // 如果图片就是第一块（index 0），直接删掉图片
                    blocks.removeAt(currentIndex - 1)
                    focusedIndex = currentIndex - 1
                }
            }
            is EditorBlock.Text -> {
                val combinedText = prevBlock.content.text + currentBlock.content.text
                val newSelection = TextRange(prevBlock.content.text.length)

                blocks[currentIndex - 1] = prevBlock.copy(
                    content = TextFieldValue(combinedText, newSelection)
                )
                blocks.removeAt(currentIndex)
                focusedIndex = currentIndex - 1
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
        focusedIndex = currentIndex + 2
    }

    private fun handleSmartBreak(oldValue: TextFieldValue, newValue: TextFieldValue): TextFieldValue? {
        fun continueListMode(newValue: TextFieldValue, prefix: String): TextFieldValue {
            val insertPos = newValue.selection.start
            return newValue.copy(
                text = newValue.text.replaceRange(
                    insertPos,
                    insertPos,
                    prefix
                ),
                selection = TextRange(insertPos + prefix.length)
            )
        }

        fun exitListMode(
            oldValue: TextFieldValue,
            lineStart: Int,
            cursor: Int
        ): TextFieldValue {
            return oldValue.copy(
                text = oldValue.text.replaceRange(
                    lineStart,
                    cursor,
                    ""
                ),
            ).let {
                val finalDocs = it.text.replaceRange(lineStart, lineStart, "")
                it.copy(text = finalDocs, selection = TextRange(lineStart))
            }
        }
        if (newValue.text.length != oldValue.text.length + 1) return null

        val cursor = newValue.selection.start
        if (cursor <= 0) return null

        val lastChar = newValue.text[cursor - 1]
        if (lastChar != '\n') return null

        val text = oldValue.text
        val oldCursor = oldValue.selection.start

        val lineStart = text.lastIndexOf('\n', oldCursor - 1).let { if (it == -1) 0 else it + 1 }
        if (lineStart > oldCursor) return null

        val currentLine = text.substring(lineStart, oldCursor)

        val unorderListRegex = Regex("""^(\s*[-*+]\s+)""")
        val orderListRegex = Regex("""^(\s*)(\d+)\.\s+""")
        val quoteRegex = Regex("""^((>\s*)+)""")

        return when {
            unorderListRegex.find(currentLine) != null -> {
                val prefix = unorderListRegex.find(currentLine)!!.value
                if (currentLine.trim() == prefix.trim()) {
                    exitListMode(oldValue, lineStart, oldCursor)
                } else {
                    continueListMode(newValue, prefix)
                }
            }
            orderListRegex.find(currentLine) != null -> {
                val match = orderListRegex.find(currentLine)!!
                val prefix = match.value
                if (currentLine.trim() == prefix.trim().removeSuffix(".")) {
                    exitListMode(oldValue, lineStart, oldCursor)
                } else {
                    val nextNum = (match.groupValues[2].toIntOrNull() ?: 0) + 1
                    val nextPrefix = "${match.groupValues[1]}$nextNum. "
                    continueListMode(newValue, nextPrefix)
                }
            }
            quoteRegex.find(currentLine) != null -> {
                val prefix = quoteRegex.find(currentLine)!!.value
                if (currentLine.trim() == ">" || currentLine.trim().isEmpty()) {
                    exitListMode(oldValue, lineStart, oldCursor)
                } else {
                    continueListMode(newValue, prefix)
                }
            }
            else -> null
        }
    }


}
val MarkdownEditorManager.focusBlock
    get() = blocks[focusedIndex] as? EditorBlock.Text ?: EditorBlock.Text()