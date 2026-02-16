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
import androidx.compose.ui.text.style.TextIndent
import androidx.compose.ui.unit.Density
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

class MarkdownVisualTransformation(
    private val colorScheme: ColorScheme,
    private val tpy: Typography,
) : VisualTransformation {
    override fun filter(text: AnnotatedString): TransformedText {
        val rawText = text.text
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
                    style.toSpanStyle().copy(fontWeight = FontWeight.Bold),
                    start,
                    end
                )
//                builder.addStyle(
//                    style = ParagraphStyle(lineHeight = 38.sp),
//                    start = start,
//                    end = end
//                )
                builder.addStyle(
                    SpanStyle(color = Color.Transparent, fontSize = 0.sp),
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
                    SpanStyle(color = Color.Transparent, fontSize = 0.sp),
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
                    addStyle(SpanStyle(color = Color.Transparent, fontSize = 0.sp), fullMatchStart, fullMatchStart + 1)
                    addStyle(SpanStyle(color = Color.Transparent, fontSize = 0.sp), fullMatchStart + 1 + textPart.length, fullMatchStart + 2 + textPart.length)
                    addStyle(SpanStyle(color = colorScheme.primary, textDecoration = TextDecoration.Underline), fullMatchStart + 1, fullMatchStart + 1 + textPart.length)
                    addStyle(SpanStyle(color = Color.Transparent, fontSize = 0.sp), fullMatchStart + textPart.length + 2, start + match.range.last + 1)
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
        val symbolStyle = SpanStyle(color = Color.Transparent, fontSize = 0.sp)
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