package com.ljp.common.baseui.markdown.render.config

import androidx.compose.ui.text.SpanStyle

data class InlineStyleConfig(
    override val codeStyle: SpanStyle,
    override val emphasisStyle: SpanStyle,
    override val strongEmphasisStyle: SpanStyle,
    override val linkStyle: SpanStyle
) : CodeStyle, EmphasisStyle, StrongEmphasisStyle, LinkStyle