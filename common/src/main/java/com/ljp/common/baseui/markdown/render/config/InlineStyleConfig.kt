package com.ljp.common.baseui.markdown.render.config

import androidx.compose.ui.text.SpanStyle
//可使用接口委派进行优化
data class InlineStyleConfig(
    override val codeStyle: SpanStyle,
    override val emphasisStyle: SpanStyle,
    override val strongEmphasisStyle: SpanStyle,
    override val linkStyle: SpanStyle
) : CodeStyle, EmphasisStyle, StrongEmphasisStyle, LinkStyle