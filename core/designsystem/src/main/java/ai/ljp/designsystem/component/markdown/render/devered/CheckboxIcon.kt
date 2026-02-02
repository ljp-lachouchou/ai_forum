package com.ljp.common.baseui.markdown.render.devered

import androidx.compose.ui.graphics.Path
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.size
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.PaintingStyle.Companion.Stroke
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.StrokeJoin
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.unit.dp

@Composable
fun CheckBoxIcon(initSelected: Boolean, modifier: Modifier = Modifier,
                 checkedColor: Color = MaterialTheme.colorScheme.primary,
                 uncheckedColor: Color = MaterialTheme.colorScheme.outline,
                 checkMarkColor: Color = Color.White
) {
    var selected by remember { mutableStateOf(initSelected) }
    val boxSize = 18.dp
    val strokeWidth = 2.dp
    Canvas(modifier.size(boxSize).clickable{
        selected = !selected
    }) {
        val width = size.width
        val height = size.height
        val cornerRadius = 4.dp.toPx()
        if (selected) {
            // 1. 绘制实心背景方框
            drawRoundRect(
                color = checkedColor,
                size = size,
                cornerRadius = CornerRadius(cornerRadius)
            )

            // 2. 绘制对钩 (Checkmark)
            // 对钩由两条线段组成：短边由左中到下中，长边由下中到右上
            val path = Path().apply {
                moveTo(width * 0.25f, height * 0.5f)  // 起点：左侧中间
                lineTo(width * 0.45f, height * 0.7f)  // 拐点：底部中心偏左
                lineTo(width * 0.75f, height * 0.3f)  // 终点：右侧上方
            }

            drawPath(
                path = path,
                color = checkMarkColor,
                style = Stroke(
                    width = strokeWidth.toPx(),
                    cap = StrokeCap.Round,
                    join = StrokeJoin.Round
                )
            )
        } else {
            drawRoundRect(
                color = uncheckedColor,
                size = size,
                cornerRadius = CornerRadius(cornerRadius),
                style = Stroke(width = strokeWidth.toPx())
            )
        }
    }

}