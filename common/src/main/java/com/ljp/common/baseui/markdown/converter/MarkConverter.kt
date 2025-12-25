package com.ljp.common.baseui.markdown.converter

import com.ljp.common.baseui.markdown.node.MarkNode
import org.commonmark.node.AbstractVisitor
import org.commonmark.node.BlockQuote
import org.commonmark.node.BulletList
import org.commonmark.node.FencedCodeBlock
import org.commonmark.node.Heading
import org.commonmark.node.IndentedCodeBlock
import org.commonmark.node.ListItem
import org.commonmark.node.Node
import org.commonmark.node.OrderedList
import org.commonmark.node.Paragraph
import org.commonmark.node.ThematicBreak

/**
 * 转换 Markdown 节点为 MarkNode 节点
 */
class MarkConverter(val urlSigner:(String)-> String): AbstractVisitor() {
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

            val inlineConverter = InlineConverter(urlSigner, imageMap)
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
            val inlineConverter = InlineConverter(urlSigner, imageMap)
            val annotatedString = inlineConverter.build(it)

            blocks.add(MarkNode.Block.Heading(heading.level, annotatedString))
        }
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
        val tempVisitor = MarkConverter(urlSigner)
        var child = parent.firstChild
        while (child != null) {
            child.accept(tempVisitor)
            child = child.next
        }
        return tempVisitor.blocks
    }
}