package com.ljp.common.baseui.markdown.converter

import androidx.compose.foundation.text.appendInlineContent
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextDecoration
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

class InlineConverter(
    private val urlSigner: (String) -> String,
    private val imageMap: MutableMap<String, String> // 用于记录 ID -> 签名后的 URL
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
        builder.pushStyle(SpanStyle(fontWeight = FontWeight.Bold))
        visitChildren(strongEmphasis) // 递归处理加粗里面的内容（可能有链接或斜体）
        builder.pop()
    }

    override fun visit(emphasis: Emphasis) {
        // 斜体：对应 *text*
        builder.pushStyle(SpanStyle(fontStyle = FontStyle.Italic))
        visitChildren(emphasis)
        builder.pop()
    }

    override fun visit(link: Link) {
        // 链接：[text](url)
        builder.pushStyle(SpanStyle(color = Color.Blue, textDecoration = TextDecoration.Underline))
        // 关键：压入注解，方便后续点击处理
        builder.pushStringAnnotation(tag = "URL", annotation = link.destination)
        visitChildren(link)
        builder.pop()
        builder.pop()
    }

    override fun visit(code: Code) {
        // 行内代码：`code` (注意：Code 在 Commonmark 里没有子节点，内容在 literal 中)
        builder.pushStyle(SpanStyle(
            background = Color.LightGray.copy(alpha = 0.2f),
            fontFamily = FontFamily.Monospace
        ))
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