package com.ljp.common.baseui.markdown.converter

import androidx.compose.foundation.text.appendInlineContent
import androidx.compose.ui.text.AnnotatedString
import com.ljp.common.baseui.markdown.render.config.InlineStyleConfig
import org.commonmark.node.AbstractVisitor
import org.commonmark.node.Code
import org.commonmark.node.Emphasis
import org.commonmark.node.HardLineBreak
import org.commonmark.node.Image
import org.commonmark.node.Link
import org.commonmark.node.Node
import org.commonmark.node.SoftLineBreak
import org.commonmark.node.StrongEmphasis
import org.commonmark.node.Text

internal class InlineConverter(
    private val urlSigner: (String) -> String,
    private val imageMap: MutableMap<String, String>,
    private val styleConfig: InlineStyleConfig
) : AbstractVisitor() {

    private val builder = AnnotatedString.Builder()

    fun build(node: Node): AnnotatedString {
        node.accept(this)
        return builder.toAnnotatedString()
    }

    override fun visit(softLineBreak: SoftLineBreak?) {
        builder.append(" ")
    }

    override fun visit(hardLineBreak: HardLineBreak?) {
        builder.append("\n")
    }

    override fun visit(text: Text) {
        // 最基础的文本：直接追加内容
        builder.append(text.literal)
    }

    override fun visit(strongEmphasis: StrongEmphasis) {
        // 加粗：对应 **text**
        builder.pushStyle(styleConfig.strongEmphasisStyle)
        visitChildren(strongEmphasis) // 递归处理加粗里面的内容（可能有链接或斜体）
        builder.pop()
    }

    override fun visit(emphasis: Emphasis) {
        // 斜体：对应 *text*
        builder.pushStyle(styleConfig.emphasisStyle)
        visitChildren(emphasis)
        builder.pop()
    }

    override fun visit(link: Link) {
        // 链接：[text](url)
        builder.pushStyle(styleConfig.linkStyle)
        // 关键：压入注解，方便后续点击处理
        builder.pushStringAnnotation(tag = "URL", annotation = link.destination)
        visitChildren(link)
        builder.pop()
        builder.pop()
    }

    override fun visit(code: Code) {
        builder.pushStyle(
            styleConfig.codeStyle
        )
        builder.append(code.literal)
        builder.pop()
    }

    override fun visit(image: Image) {
        // 行内图片：![alt](url) -> 挖坑
        val id = "inline_img_${image.hashCode()}"
        val signedUrl = urlSigner(image.destination)
        imageMap[id] = signedUrl

        builder.appendInlineContent(id, "[图]")
    }
}