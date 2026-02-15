package ai.ljp.designsystem.component.markdown.render.tool

import androidx.compose.material3.ColorScheme
import androidx.compose.material3.Typography
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.SpanStyle
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
            val hMatch = headMatch(line)
            if(hMatch != null) {
                val level = hMatch.groupValues[1].length
                val style = getHeadingStyle(level)
                builder.addStyle(
                    style.toSpanStyle().copy(fontWeight = FontWeight.Bold),
                    start,
                    end
                )
                builder.addStyle(
                    SpanStyle(color = Color.Transparent,
                        fontSize = 0.sp),
                    start,
                    start + level + 1
                )
            }
            if (line.startsWith("> ")) {
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
            val lMatch = listMatch(line)
            if (lMatch != null) {
                builder.addStyle(
                    SpanStyle(color = colorScheme.primary, fontWeight = FontWeight.Bold),
                    start + lMatch.range.first,
                    start + lMatch.range.last + 1
                )
            }
            linkMatch(line)
            .forEach { match ->
                val textPart = match.groupValues[1]
                val fullMatchStart = start + match.range.first

                // 链接文本高亮
                builder.addStyle(
                    SpanStyle(color = colorScheme.primary, textDecoration = TextDecoration.Underline),
                    fullMatchStart + 1, fullMatchStart + 1 + textPart.length
                )
                // 隐藏/淡化 URL 部分，保持视觉简洁
                builder.addStyle(
                    SpanStyle(color = colorScheme.outline.copy(0.3f), fontSize = 12.sp),
                    fullMatchStart + textPart.length + 2, start + match.range.last + 1
                )
            }

            if (index < lines.size - 1) {
                builder.append("\n")
                currentOffset = end + 1
            }
        }
        return TransformedText(builder.toAnnotatedString(), OffsetMapping.Identity)
        }
    private fun MarkdownVisualTransformation.getHeadingStyle(level: Int) = when(level) {
        1 -> tpy.headlineLarge
        2 -> tpy.headlineMedium
        3 -> tpy.headlineSmall
        4 -> tpy.titleLarge
        else -> tpy.titleMedium
    }
}
fun headMatch(line : String) =
    Regex("^(#{1,6})\\s+").find(line)

fun listMatch(line : String) =
    Regex("^(\\s*)([*\\-+]|\\d+\\.)\\s+").find(line)

fun linkMatch(line: String) =
    Regex("\\[(.*?)]\\((.*?)\\)").findAll(line)



