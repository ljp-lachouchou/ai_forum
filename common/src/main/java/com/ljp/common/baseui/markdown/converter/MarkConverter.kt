package com.ljp.common.baseui.markdown.converter

import androidx.compose.ui.text.AnnotatedString
import com.ljp.common.baseui.markdown.node.MarkNode
import com.ljp.common.baseui.markdown.node.TableCellData
import com.ljp.common.baseui.markdown.render.config.InlineStyleConfig
import org.commonmark.ext.gfm.tables.TableBlock
import org.commonmark.ext.gfm.tables.TableBody
import org.commonmark.ext.gfm.tables.TableCell
import org.commonmark.ext.gfm.tables.TableHead
import org.commonmark.ext.gfm.tables.TableRow
import org.commonmark.node.AbstractVisitor
import org.commonmark.node.BlockQuote
import org.commonmark.node.BulletList
import org.commonmark.node.CustomBlock
import org.commonmark.node.FencedCodeBlock
import org.commonmark.node.Heading
import org.commonmark.node.IndentedCodeBlock
import org.commonmark.node.ListItem
import org.commonmark.node.Node
import org.commonmark.node.OrderedList
import org.commonmark.node.Paragraph
import org.commonmark.node.ThematicBreak
import kotlin.also

/**
 * 转换 Markdown 节点为 MarkNode 节点
 */
internal class MarkConverter(val urlSigner:(String)-> String,
                             private val styleConfig: InlineStyleConfig):
    AbstractVisitor() {
    val blocks = mutableListOf<MarkNode.Block>()
    override fun visit(blockQuote: BlockQuote?) {
        blockQuote?.let {
            val children = collectChildren(it)
            blocks.add(MarkNode.Block.BlockQuote(children))
        }
    }

    override fun visit(bulletList: BulletList?) {
        bulletList?.let {
            val children = collectChildren(it)
            blocks.add(MarkNode.Block.BulletList(children))
        }
    }
    override fun visit(orderedList: OrderedList?) {
        orderedList?.let {
            val children = collectChildren(it)
            blocks.add(MarkNode.Block.OrderedList(it.markerStartNumber,children))
        }
    }
    override fun visit(paragraph: Paragraph?) {
        paragraph?.let {
            val imageMap = mutableMapOf<String, String>()

            val inlineConverter = InlineConverter(urlSigner, imageMap,styleConfig)
            val annotatedString = inlineConverter.build(it)

            blocks.add(MarkNode.Block.Paragraph(annotatedString, imageMap))
        }
    }

    override fun visit(fencedCodeBlock: FencedCodeBlock?) {
        fencedCodeBlock?.let {
            blocks.add(MarkNode.Block.CodeBlock(it.literal, it.info, true))
        }
    }

    override fun visit(heading: Heading?) {
        heading?.let {
            val imageMap = mutableMapOf<String, String>()
            val inlineConverter = InlineConverter(urlSigner, imageMap,styleConfig)
            val annotatedString = inlineConverter.build(it)

            blocks.add(MarkNode.Block.Heading(heading.level, annotatedString))
        }
    }

    override fun visit(customBlock: CustomBlock?) {
        customBlock?.also {
            if (it is TableBlock) {
                val headData = mutableListOf<TableCellData>()
                val rowsData = mutableListOf<List<TableCellData>>()
                var child = customBlock.firstChild
                while (child != null) {
                    when (child) {
                        is TableHead -> {
                            val row = child.firstChild as? TableRow
                            row?.also {rw->
                                headData.addAll(_extractRowData(rw))
                            }
                        }
                        is TableBody -> {
                            var tableRow = child.firstChild
                            while (tableRow != null) {
                                if (tableRow is TableRow) {
                                    rowsData.add(_extractRowData(tableRow))
                                }
                                tableRow = tableRow.next
                            }
                        }
                    }
                    child = child.next
                }
                blocks.add(MarkNode.Block.Table(headData,rowsData))
            }else {
                super.visit(it)
            }
        }
    }

    private fun _extractRowData(row: TableRow): List<TableCellData> {
        val cells = mutableListOf<TableCellData>()

        var cell = row.firstChild
        while (cell != null) {
            if (cell is TableCell) {
                val cellImageMap = mutableMapOf<String, String>()
                val builder = AnnotatedString.Builder()

                var inlineNode = cell.firstChild
                while (inlineNode != null) {
                    val converter = InlineConverter(urlSigner, cellImageMap, styleConfig)
                    builder.append(converter.build(inlineNode))
                    inlineNode = inlineNode.next
                }

                cells.add(TableCellData(builder.toAnnotatedString(), cellImageMap))
            }
            cell = cell.next
        }
        return cells
    }

    override fun visit(indentedCodeBlock: IndentedCodeBlock?) {
        indentedCodeBlock?.let {
            blocks.add(MarkNode.Block.CodeBlock(it.literal, null, false))
        }
    }

    override fun visit(thematicBreak: ThematicBreak?) {
        thematicBreak?.let {
            blocks.add(MarkNode.Block.ThematicBreak)
        }
    }


    /**
     * 列表项
     */
    override fun visit(listItem: ListItem?) {
        listItem?.let {
            val children = collectChildren(it)

             blocks.add(MarkNode.Block.ListItem(children))
        }
    }

    /**
     * 收集子节点
     */
    private fun collectChildren(parent: Node): List<MarkNode.Block> {
        val tempVisitor = MarkConverter(urlSigner,styleConfig)
        var child = parent.firstChild
        while (child != null) {
            child.accept(tempVisitor)
            child = child.next
        }
        return tempVisitor.blocks
    }
}