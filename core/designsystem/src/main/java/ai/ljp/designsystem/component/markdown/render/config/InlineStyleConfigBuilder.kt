package ai.ljp.designsystem.component.markdown.render.config

import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextDecoration
import com.ljp.common.baseui.markdown.render.config.InlineStyleConfig

class InlineStyleConfigBuilder {
    private val strongEmphasisFontWeight: FontWeight = FontWeight.Bold
    private val linkTextDecoration = TextDecoration.Underline
    private val emphasisFontStyle = FontStyle.Italic
    var linkColor: Color = Color.Blue

    var codeBackgroundColor: Color = Color.LightGray
    var codeBackgroundAlpha: Float = 0.2f
    var codeFont = FontFamily.Monospace
    fun build() = InlineStyleConfig(
        SpanStyle(
            background = codeBackgroundColor.copy(codeBackgroundAlpha),
            fontFamily = codeFont
        ),
        SpanStyle(fontStyle = emphasisFontStyle),
        SpanStyle(fontWeight = strongEmphasisFontWeight),
        SpanStyle(color = linkColor, textDecoration = linkTextDecoration),
    )
}
