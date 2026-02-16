package ai.ljp.designsystem.component.markdown.render.tool

import androidx.compose.material3.ColorScheme
import androidx.compose.material3.Typography
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.ParagraphStyle
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.OffsetMapping
import androidx.compose.ui.text.input.TransformedText
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.sp

class MarkdownVisualTransformation(
    private val colorScheme: ColorScheme,
    private val tpy: Typography
) : VisualTransformation {
    override fun filter(text: AnnotatedString): TransformedText {
        val  rawText = text.text
        val builder = AnnotatedString.Builder()

        val lines = rawText.split("\n")
        var currentOffset = 0
        lines.forEachIndexed { index,line ->
            val start = currentOffset
            val end = currentOffset + line.length
            builder.append(line)
            inlineFilter(
                builder = builder,
                rawText = line,
                currentOffset = currentOffset
            )
            headMatch(line) {hMatch ->
                val level = hMatch.groupValues[1].length
                val style = getHeadingStyle(level)
                builder.addStyle(
                    style.toSpanStyle().copy(
                        fontWeight = FontWeight.Bold,

                    ),
                    start,
                    end
                )
                builder.addStyle(
                    style = ParagraphStyle(lineHeight = 38.sp),
                    start = start,
                    end = end
                )
                builder.addStyle(
                    SpanStyle(color = Color.Transparent,
                        fontSize = 0.sp),
                    start,
                    start + level + 1
                )
            }
            quoteAction(line) {
                builder.addStyle(
                    SpanStyle(
                        color = colorScheme.secondary,
                        fontStyle = FontStyle.Italic,
                        background = colorScheme.secondaryContainer.copy(0.2f)
                    ),
                    start,
                    end
                )
                builder.addStyle(
                    SpanStyle(color = Color.Transparent, fontSize = 0.sp),
                    start, start + 2
                )

            }
            unOrderListMatch(line) {lMatch->
                builder.addStyle(
                    SpanStyle(
                        color = Color.Transparent,
                        fontSize = 0.sp
                        ),
                    start + lMatch.range.first,
                    start + lMatch.range.last + 1
                )
            }
            orderListMatch(line) {lMatch->
                builder.addStyle(
                    SpanStyle(
                        color = colorScheme.primary,
                        fontWeight = FontWeight.Bold
                    ),
                    start + lMatch.range.first,
                    start + lMatch.range.last + 1
                )
            }
            linkMatch(line) {match ->
                val textPart = match.groupValues[1]
                val fullMatchStart = start + match.range.first
                builder.apply {
                    addStyle(
                        SpanStyle(color = Color.Transparent,
                            fontSize = 0.sp),
                        fullMatchStart,
                        fullMatchStart + 1
                    )
                    addStyle(
                        SpanStyle(color = Color.Transparent,
                            fontSize = 0.sp),
                        fullMatchStart + 1 + textPart.length,
                        fullMatchStart + 2 + textPart.length
                    )
                    addStyle(
                        SpanStyle(color = colorScheme.primary, textDecoration = TextDecoration.Underline),
                        fullMatchStart + 1, fullMatchStart + 1 + textPart.length
                    )
                    // 链接文本高亮
                    // 隐藏/淡化 URL 部分，保持视觉简洁
                    addStyle(
                        SpanStyle(
                            color = Color.Transparent,
                            fontSize = 0.sp
                        ),
                        fullMatchStart + textPart.length + 2, start + match.range.last + 1
                    )
                }

            }
            if (index < lines.size - 1) {
                builder.append("\n")
                currentOffset = end + 1
            }
        }
        return TransformedText(builder.toAnnotatedString(), OffsetMapping.Identity)
        }

    private fun inlineFilter(
        builder: AnnotatedString.Builder,
        rawText: String,
        currentOffset: Int,

        ) {
        codeMatch(rawText) {match->
            applyStyle(
                builder = builder,
                match = match,
                SpanStyle(
                    fontFamily = FontFamily.Monospace,
                    background = colorScheme.surfaceVariant,
                    color = colorScheme.primary
                ),
                currentOffset =currentOffset
            )
        }
        boldItalicMatch(rawText) { match ->
            applyStyle(builder, match, SpanStyle(
                fontWeight = FontWeight.Bold,
                fontStyle = FontStyle.Italic
            ), currentOffset = currentOffset)
        }
        boldMatch(rawText) {matchResult ->
            applyStyle(
                builder,
                matchResult,
                SpanStyle(fontWeight = FontWeight.Bold),
                currentOffset = currentOffset
            )
        }
        italicMatch(rawText) {matchResult ->
            applyStyle(
                builder,
                matchResult,
                SpanStyle(fontStyle = FontStyle.Italic),
                currentOffset = currentOffset
            )
        }
        strikeMatch(rawText) {matchResult ->
            applyStyle(
                builder,
                matchResult,
                SpanStyle(textDecoration = TextDecoration.LineThrough),
                currentOffset = currentOffset
            )
        }
    }

    private fun MarkdownVisualTransformation.getHeadingStyle(level: Int) = when(level) {
        1 -> tpy.headlineLarge
        2 -> tpy.headlineMedium
        3 -> tpy.headlineSmall
        4 -> tpy.titleLarge
        else -> tpy.titleMedium
    }
    private fun applyStyle(
        builder: AnnotatedString.Builder,
        match: MatchResult,
        style: SpanStyle,
        currentOffset: Int
    ) {
        val fullRange = match.range
        val contentGroup = match.groups[1] ?: return // 获取括号捕获的内容

        // 1. 全文绝对索引计算
        val startInFull = currentOffset + fullRange.first
        val endInFull = currentOffset + fullRange.last + 1
        val contentStartInFull = currentOffset + contentGroup.range.first
        val contentEndInFull = currentOffset + contentGroup.range.last + 1

        // 2. 应用主体样式（如加粗、斜体）到内容部分
        builder.addStyle(style, contentStartInFull, contentEndInFull)

        // 3. 精细控制符号样式：淡化符号
        val symbolStyle = SpanStyle(
            color = Color.Transparent,
            fontSize = 0.sp
        )

        // 左侧符号：从匹配开始到内容开始
        builder.addStyle(symbolStyle, startInFull, contentStartInFull)
        // 右侧符号：从内容结束到匹配结束
        builder.addStyle(symbolStyle, contentEndInFull, endInFull)
    }
}
fun headMatch(line : String,block :(MatchResult) -> Unit) {
    val hMatch = Regex("^(#{1,6})\\s+").find(line)
    if (hMatch == null) return
    block(hMatch)
}

fun listMatch(line : String,block :(MatchResult) -> Unit) {
    val lMatch = Regex("^(\\s*)([*\\-+]|\\d+\\.)\\s+")
        .find(line)
    if (lMatch == null) return
    block(lMatch)
}


fun linkMatch(line: String,action : (MatchResult) -> Unit) {
    val matches = Regex("\\[(.*?)]\\((.*?)\\)").findAll(line)
    matches.forEach(action)
}
fun quoteAction(line: String,block : (String) -> Unit) {
    if (line.startsWith("> ")) {
        block(line)
    }
}
fun unOrderListMatch(line: String,action : (MatchResult) -> Unit) {
    val matches = Regex("""^(> \s*)*(\s*)(-)\s+""").findAll(line)
    matches.forEach(action)
}
fun String.unOrderListMatchFound( ): Boolean {
    return Regex("""^(> \s*)*(\s*)(-)\s+""").findAll(this).toList().isNotEmpty()
}
fun orderListMatch(line : String,action : (MatchResult) -> Unit) {
    val matches = Regex("""^(\s*)(\d+)\.\s+""").findAll(line)
    matches.forEach(action)
}
fun codeMatch(line : String,action : (MatchResult) -> Unit) {
    val matches = Regex("`([^`]+)`").findAll(line)
    matches.forEach(action)
}
fun boldMatch(line: String,action: (MatchResult) -> Unit) {
    val matches = Regex("\\*\\*(.*?)\\*\\*").findAll(line)
    matches.forEach(action)
}
fun italicMatch(line : String,action: (MatchResult) -> Unit) {
    val matches =
        Regex("""(?<!\*)\*(?!\*)([^*]+)(?<!\*)\*(?!\*)""")
            .findAll(line)
    matches.forEach(action)
}
fun strikeMatch(line : String,action: (MatchResult) -> Unit) {
    val matches = Regex("~~(.+?)~~").findAll(line)
    matches.forEach(action)
}
fun symbolMatch(line: String,action: (MatchResult) -> Unit) {
    val matches = Regex("[*_~`]+").findAll(line)
    matches.forEach(action)
}
fun boldItalicMatch(line: String, action: (MatchResult) -> Unit) {
    // 匹配 ***text***
    val matches = Regex("""\*\*\*(.*?)\*\*\*""").findAll(line)
    matches.forEach(action)
}




