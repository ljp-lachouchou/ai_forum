package ai.ljp.aiforum

import ai.ljp.aiforum.ui.theme.AIForumTheme

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.unit.dp
import com.ljp.common.baseui.markdown.render.actual.MarkdownView
import com.ljp.common.event.observeEvent
import com.ljp.common.log.core.printer.i
import com.ljp.common.log.core.priority.LogcatPriorityInstance
import io.ljp.simapi.ExampleViewModel
import kotlin.getValue
import kotlin.math.PI
import kotlin.math.cos
import kotlin.math.sin

class SecondActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        val vm by viewModels<ExampleViewModel>()
        observeEvent("USER_CLICK_ACTION","SecondScreen", isSticky = true)
        setContent {
            i(LogcatPriorityInstance,"hello")
            AIForumTheme {
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    MarkdownView("""
为了全面测试你编写的渲染组件（尤其是处理 `onLinkClick`、`inlineContent` 图片占位符、以及代码块滚动等功能），我为你准备了这份包含 **Markdown 全语法** 的测试文本。

你可以将这段文本直接作为测试输入传给你的 `MarkdownRenderer`。

---
# 🌟 Markdown 渲染测试大全 (H1)

## 1. 文本样式测试 (H2)

这是一段普通文本。我们可以在其中加入 **加粗 (Bold)**、*斜体 (Italic)*、***粗斜体 (Bold & Italic)*** 以及 ~~删除线 (Strikethrough)~~。

测试行内链接：[点击跳转到 Google](https://www.google.com)

---

## 2. 列表与嵌套测试 (H2)

### 无序列表 (H3)

* 第一项
* 第二项
  * 嵌套子项 A
  * 嵌套子项 B


* 第三项

### 有序列表

1. 第一步：解析 Markdown
  1. 我的世界
- 你好
  - nil
2. 第二步：转换组件
  2. 我的世界
  3. Nih
3. 第三步：UI 渲染
  5. kkk
---

## 3. 引用与分隔线 (H2)

> 这是一段引用块（BlockQuote）。
> 它可以包含多行文字。
> > 甚至可以支持**嵌套引用**。
> 
> 

上面和下面各有一条分隔线（Thematic Break）。

---

## 4. 代码块测试 (H2)

行内代码测试：使用 `Modifier.padding()` 来增加间距。

**Kotlin 代码块（测试横向滚动）：**

```kotlin
@Composable
fun MarkdownParagraph(node: MarkNode.Block.Paragraph, onLinkClick: (String) -> Unit) {
    // 这是一个非常长的注释，用来测试你的 MarkdownCodeBlock 组件是否支持水平方向的滚动条，确保长代码不会被截断。
    val textStyle = MaterialTheme.typography.bodyLarge.copy(color = MaterialTheme.colorScheme.primary)
    Text(text = node.content, modifier = Modifier.padding(8.dp), style = textStyle)
}

```
---

## 5. 图片测试 (H2)
这里包含一张行内图片:  
a
![alt](https://book-1369048677.cos.ap-beijing.myqcloud.com/img-313e1e7f98d05fdf819fb2324519c258.png)
![alt](https://book-1369048677.cos.ap-beijing.myqcloud.com/img-313e1e7f98d05fdf819fb2324519c258.png)
a
![alt](https://book-1369048677.cos.ap-beijing.myqcloud.com/img-313e1e7f98d05fdf819fb2324519c258.png)
以及一张带描述的图片：  
[点击跳转到 Google](https://www.google.com)
![alt](https://book-1369048677.cos.ap-beijing.myqcloud.com/img-313e1e7f98d05fdf819fb2324519c258.png)
![alt](https://book-1369048677.cos.ap-beijing.myqcloud.com/img-313e1e7f98d05fdf819fb2324519c258.png)
[点击跳转到 Google](https://www.google.com)  
[点击跳转到 Google](https://www.google.com)

---

## 6. 表格测试 (H2)

| 姓名 | 职业 | 技能点 | 备注 |
| --- | --- | --- | --- |
| Gemini | AI 助手 | 理解、生成、逻辑 | 勤奋工作 |
| 开发人员 | 程序员 | Kotlin, Compose, Debug | 头发尚在 |
| 测试员 | QA | ![alt](https://book-1369048677.cos.ap-beijing.myqcloud.com/img-313e1e7f98d05fdf819fb2324519c258.png)| [点击跳转到 Google](https://www.google.com) |

---

### 7. 任务列表 (H3)

* [x] 已完成环境配置
* [x] 解决了 Gradle 编译报错
* [ ] 完成 Markdown 渲染器开发
* [ ] 发布第一个版本

---

### 如何使用这段文本进行调试？

1. **检查 H1 - H6**：确认字体大小是否有明显的阶梯感。
2. **点击链接**：点击 `https://www.google.com` 确认是否能通过 `onLinkClick` 触发 Toast 或浏览器跳转。
3. **横向滑动代码**：在手机上左右滑动 Kotlin 代码块，确认 `horizontalScroll` 是否生效。
4. **观察引用块**：确认左侧的 `VerticalDivider` 颜色是否能清晰区分内容。

**如果渲染结果中有任何样式不如预期（比如表格挤在一起或图片无法显示），你可以告诉我，我们针对性地调整对应的 Compose 函数。你想让我先帮你处理表格（Table）的渲染逻辑吗？**
                    """.trimIndent(),{
                        codeBackgroundColor = Color.Red
                    })
                }
            }
        }
    }
}
@Composable
fun AnimatedCircularProgress(
    targetProgress: Float, // 0.0 到 1.0
    modifier: Modifier = Modifier
) {
    // 1. 定义动画进度
    val animatedProgress by animateFloatAsState(
        targetValue = targetProgress,
        animationSpec = tween(durationMillis = 1000, easing = FastOutSlowInEasing),
        label = "progress"
    )

    Canvas(modifier = modifier.size(200.dp)) {
        val strokeWidth = 15.dp.toPx()
        val center = Offset(size.width / 2, size.height / 2)
        val radius = (size.minDimension - strokeWidth) / 2

        // 2. 绘制背景灰圆环
        drawCircle(
            color = Color.LightGray.copy(alpha = 0.3f),
            radius = radius,
            center = center,
            style = Stroke(width = strokeWidth)
        )

        // 3. 绘制彩色进度弧形
        // 我们使用 SweepGradient 制造炫酷的旋转渐变感
        drawArc(
            brush = Brush.sweepGradient(
                colors = listOf(Color(0xFF6200EE), Color(0xFF03DAC5), Color(0xFF6200EE)),
                center = center
            ),
            startAngle = -90f, // 从 12 点钟方向开始
            sweepAngle = 360f * animatedProgress,
            useCenter = false,
            style = Stroke(width = strokeWidth, cap = StrokeCap.Round)
        )

        // 4. 绘制进度末端的小发光点 (装饰)
        val angleInRadians = (360f * animatedProgress - 90f) * (PI / 180f).toFloat()
        val pointerCenter = Offset(
            x = center.x + radius * cos(angleInRadians),
            y = center.y + radius * sin(angleInRadians)
        )
        val paint = android.graphics.Paint().apply {
            color = android.graphics.Color.WHITE
            setShadowLayer(10f, 0f, 0f, android.graphics.Color.WHITE) // 设置阴影/发光
            isAntiAlias = true
        }

        drawCircle(
            color = Color.White,
            radius = strokeWidth / 3f,
            center = pointerCenter
        )
    }
}



