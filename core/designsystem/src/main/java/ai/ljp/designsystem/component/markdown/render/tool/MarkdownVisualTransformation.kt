package ai.ljp.designsystem.component.markdown.render.tool

import androidx.compose.material3.ColorScheme
import androidx.compose.material3.Typography
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.AnnotatedString
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
    private val tpy: Typography,
) : VisualTransformation {

    // 定义“似有似无”的样式符样式
    private val symbolStyle = SpanStyle(
        color = Color.Gray.copy(alpha = 0.3f), // 低透明度灰色
        fontSize = 11.sp,                     // 较小的字号
        fontFamily = FontFamily.Monospace,    // 代码质感的等宽字体
        fontWeight = FontWeight.Normal
    )

    override fun filter(text: AnnotatedString): TransformedText {
        val rawText = text.text
        val builder = AnnotatedString.Builder()

        val lines = rawText.split("\n")
        var currentOffset = 0
        lines.forEachIndexed { index, line ->
            val start = currentOffset
            val end = currentOffset + line.length
            builder.append(line)

            inlineFilter(
                builder = builder,
                rawText = line,
                currentOffset = currentOffset
            )

            headMatch(line) { hMatch ->
                val level = hMatch.groupValues[1].length
                val style = getHeadingStyle(level)
                builder.addStyle(
                    style.toSpanStyle().copy(fontWeight = FontWeight.Bold),
                    start,
                    end
                )
                // 展现标题符号 #
                builder.addStyle(symbolStyle, start, start + level + 1)
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
                // 展现引用符号 >
                builder.addStyle(symbolStyle, start, start + 2)
            }

            unOrderListMatch(line) { lMatch ->
                // 展现无序列表符号 -
                builder.addStyle(
                    symbolStyle,
                    start + lMatch.range.first,
                    start + lMatch.range.last + 1
                )
            }

            orderListMatch(line) { lMatch ->
                builder.addStyle(
                    SpanStyle(color = colorScheme.primary, fontWeight = FontWeight.Bold),
                    start + lMatch.range.first,
                    start + lMatch.range.last + 1
                )
            }

            linkMatch(line) { match ->
                val textPart = match.groupValues[1]
                val fullMatchStart = start + match.range.first
                builder.apply {
                    // 展现链接符号 [ ] ( )
                    addStyle(symbolStyle, fullMatchStart, fullMatchStart + 1)
                    addStyle(symbolStyle, fullMatchStart + 1 + textPart.length, fullMatchStart + 2 + textPart.length)
                    addStyle(SpanStyle(color = colorScheme.primary, textDecoration = TextDecoration.Underline), fullMatchStart + 1, fullMatchStart + 1 + textPart.length)
                    addStyle(symbolStyle, fullMatchStart + textPart.length + 2, start + match.range.last + 1)
                }
            }

            if (index < lines.size - 1) {
                builder.append("\n")
                currentOffset = end + 1
            }
        }
        return TransformedText(builder.toAnnotatedString(), OffsetMapping.Identity)
    }

    private fun inlineFilter(builder: AnnotatedString.Builder, rawText: String, currentOffset: Int) {
        codeMatch(rawText) { match ->
            applyStyle(builder, match, SpanStyle(fontFamily = FontFamily.Monospace, background = colorScheme.surfaceVariant, color = colorScheme.primary), currentOffset)
        }
        boldItalicMatch(rawText) { match ->
            applyStyle(builder, match, SpanStyle(fontWeight = FontWeight.Bold, fontStyle = FontStyle.Italic), currentOffset)
        }
        boldMatch(rawText) { match ->
            applyStyle(builder, match, SpanStyle(fontWeight = FontWeight.Bold), currentOffset)
        }
        italicMatch(rawText) { match ->
            applyStyle(builder, match, SpanStyle(fontStyle = FontStyle.Italic), currentOffset)
        }
        strikeMatch(rawText) { match ->
            applyStyle(builder, match, SpanStyle(textDecoration = TextDecoration.LineThrough), currentOffset)
        }
    }

    private fun getHeadingStyle(level: Int) = when (level) {
        1 -> tpy.headlineLarge
        2 -> tpy.headlineMedium
        3 -> tpy.headlineSmall
        4 -> tpy.titleLarge
        else -> tpy.titleMedium
    }

    private fun applyStyle(builder: AnnotatedString.Builder, match: MatchResult, style: SpanStyle, currentOffset: Int) {
        val contentGroup = match.groups[1] ?: return
        val startInFull = currentOffset + match.range.first
        val endInFull = currentOffset + match.range.last + 1
        val contentStartInFull = currentOffset + contentGroup.range.first
        val contentEndInFull = currentOffset + contentGroup.range.last + 1

        builder.addStyle(style, contentStartInFull, contentEndInFull)
        // 展现行内样式符号如 ** , * , ~~ , `
        builder.addStyle(symbolStyle, startInFull, contentStartInFull)
        builder.addStyle(symbolStyle, contentEndInFull, endInFull)
    }
}
fun headMatch(line : String,block :(MatchResult) -> Unit) {
    val hMatch = Regex("^(#{1,6})\\s+").find(line)
    if (hMatch == null) return
    block(hMatch)
}

fun linkMatch(line: String, action: (MatchResult) -> Unit) {
    Regex("\\[(.*?)]\\((.*?)\\)").findAll(line).forEach(action)
}

fun quoteAction(line: String, block: (String) -> Unit) {
    if (line.startsWith("> ")) block(line)
}

fun unOrderListMatch(line: String, action: (MatchResult) -> Unit) {
    Regex("""^(?>\s*)*(?:\s*)(-)\s+""").findAll(line).forEach(action)
}

fun String.unOrderListMatchFound(): Boolean {
    return Regex("""^(?>\s*)*(?:\s*)(-)\s+""").containsMatchIn(this)
}

fun orderListMatch(line: String, action: (MatchResult) -> Unit) {
    Regex("""^(\s*)(\d+)\.\s+""").findAll(line).forEach(action)
}

fun codeMatch(line: String, action: (MatchResult) -> Unit) {
    Regex("`([^`]+)`").findAll(line).forEach(action)
}

fun boldMatch(line: String, action: (MatchResult) -> Unit) {
    Regex("\\*\\*(.*?)\\*\\*").findAll(line).forEach(action)
}

fun italicMatch(line: String, action: (MatchResult) -> Unit) {
    Regex("""(?<!\*)\*(?!\*)([^*]+)(?<!\*)\*(?!\*)""").findAll(line).forEach(action)
}

fun strikeMatch(line: String, action: (MatchResult) -> Unit) {
    Regex("~~(.+?)~~").findAll(line).forEach(action)
}

fun boldItalicMatch(line: String, action: (MatchResult) -> Unit) {
    Regex("""\*\*\*(.*?)\*\*\*""").findAll(line).forEach(action)
}