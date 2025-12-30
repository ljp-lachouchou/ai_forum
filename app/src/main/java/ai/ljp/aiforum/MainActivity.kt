package ai.ljp.aiforum

import android.os.Bundle
import androidx.navigation.compose.composable
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text

import androidx.compose.ui.tooling.preview.Preview
import ai.ljp.aiforum.ui.theme.AIForumTheme
import android.content.Intent
import android.util.Log
import androidx.activity.viewModels
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.material3.Button
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.navigation.NavGraphBuilder
import androidx.navigation.toRoute
import com.ljp.common.baseui.markdown.render.actual.MarkdownView
import com.ljp.common.event.EventHandler
import com.ljp.common.event.NavEvent
import com.ljp.common.event.asInteractionEvent
import com.ljp.common.event.asNavEvent
import com.ljp.common.event.observeEvent
import com.ljp.common.event.publish
import com.ljp.common.log.core.printer.e
import com.ljp.common.log.core.printer.i
import com.ljp.common.log.core.printer.simName
import com.ljp.common.log.core.priority.LogcatPriorityInstance
import com.ljp.common.navigation.BaseActivity
import com.ljp.common.navigation.Screen

import io.ljp.simapi.ExampleViewModel
import kotlin.reflect.KClass

class MainActivity : BaseActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        observeEvent("USER_CLICK_ACTION","MainScreen")

    }

    override val startDestination: Screen
        get() = Screen.MainPage.Home

    override fun appHost(): NavGraphBuilder.() -> Unit =  {
        composable<Screen.MainPage.Home> {
            val vm by viewModels<ExampleViewModel>()
            Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                Column {
                    Greeting(
                        name = "Android",
                        modifier = Modifier.padding(innerPadding).clickable {
                            val clickTask: EventHandler = {
                                i(LogcatPriorityInstance, "执行了点击后的具体业务逻辑！")
                                vm.fetchData()
                            }
                            clickTask
                                .asInteractionEvent("USER_CLICK_ACTION",
                                    "MainScreen",
                                    "SecondScreen"
                                )
                                .publish()
                        }
                    )
                    Button(onClick = {
                        Screen.MainPage.Find(id = "sadasdsa")
                            .asNavEvent(this@MainActivity.simName).publish()
                        Log.e("nihao","nihao")
                    }) {
                        Text("跳转")
                    }

                }

            }
        }
        composable<Screen.MainPage.Find> {
            val (id) = it.toRoute<Screen.MainPage.Find>()
            e(LogcatPriorityInstance,"second $id")
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
                    linkColor = Color.Cyan
                })
            }
        }
    }
}

@Composable
fun Greeting(name: String, modifier: Modifier = Modifier) {
    Text(
        text = "Hello $name!",
        modifier = modifier
    )
}

@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    AIForumTheme {
        Greeting("Android")
    }
}