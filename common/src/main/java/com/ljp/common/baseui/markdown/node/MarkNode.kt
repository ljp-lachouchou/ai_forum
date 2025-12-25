package com.ljp.common.baseui.markdown.node

/**
 *  Markdown 节点
 */
import androidx.compose.ui.text.AnnotatedString

/**
 * | **节点 (Java)**       | **对应的 Markdown 语法**  | **功能描述**                                       |
 * | --------------------- | ------------------------- | -------------------------------------------------- |
 * | **Document**          | (整个文档)                | 根节点，包含所有 Block 节点。                      |
 * | **Heading**           | `# H1` ... `###### H6`    | 标题节点，包含 `level` 属性。                      |
 * | **Paragraph**         | `Hello World`             | 普通段落，是行内元素的容器。                       |
 * | **BulletList**        | `- item`                  | 无序列表容器。                                     |
 * | **OrderedList**       | `1. item`                 | 有序列表容器，包含起始数字。                       |
 * | **ListItem**          | (列表中的每一项)          | 列表项，通常内部包含一个 Paragraph 或另一个 List。 |
 * | **FencedCodeBlock**   | ````kotlin`               | 带围栏的代码块，支持语言标识。                     |
 * | **IndentedCodeBlock** | `    code`                | 通过缩进定义的代码块。                             |
 * | **BlockQuote**        | `> quote`                 | 引用块，可以嵌套其他块。                           |
 * | **StrongEmphasis**    | `**bold**`                | 加粗行内节点。                                     |
 * | **Emphasis**          | `*italic*`                | 斜体行内节点。                                     |
 * | **Link**              | `[text](url)`             | 超链接。                                           |
 * | **Image**             | `![alt](url)`             | 图片。                                             |
 * | **Code**              | ``inline code``           | 行内代码。                                         |
 * | **ThematicBreak**     | `---` 或 `***`            | 分隔线（水平线）。                                 |
 * | **TableBlock...**     | `                         | col                                                |
 * | **SoftLineBreak**     | (结尾普通换行)            | 软换行，渲染时通常视为空格。                       |
 * | **HardLineBreak**     | `      ` (两个空格加回车) | 硬换行，必须触发换行。                             |
 *
 */
enum class TableCellAlignment { LEFT, CENTER, RIGHT }
data class TableCellData(
    val text: AnnotatedString,
    val images: Map<String, String> // 存储该单元格对应的 ID -> URL 映射
)
sealed class MarkNode {
    // 块级节点：对应 LazyColumn 的独立 Item
    sealed class Block : MarkNode() {
        data class Heading(val level: Int, val content: AnnotatedString) : Block()
        data class Paragraph(val content: AnnotatedString, val imageMap: Map<String, String>) : Block()
        data class BulletList(val items: List<Block>) : Block()
        data class OrderedList(val startNumber: Int, val items: List<Block>) : Block()
        data class ListItem(val children: List<Block>) : Block()
        data class CodeBlock(val code: String, val language: String?, val isFenced: Boolean) : Block()

        data class BlockQuote(val children: List<Block>) : Block()
        object ThematicBreak : Block() // 分隔线 ---
        data class Table(
            val head: List<TableCellData>,
            val rows: List<List<TableCellData>>,
            val alignments: List<TableCellAlignment> = emptyList()
        ) : Block()
    }

    // 行内节点信息：通常在解析段落时作为辅助数据
    sealed class Inline {
        data class Text(val literal: String) : Inline()
        data class Bold(val child: Inline) : Inline()
        data class Italic(val child: Inline) : Inline()
        data class Link(val url: String, val text: String) : Inline()
        data class Image(val url: String, val alt: String) : Inline()
        data class InlineCode(val code: String) : Inline()
    }
}