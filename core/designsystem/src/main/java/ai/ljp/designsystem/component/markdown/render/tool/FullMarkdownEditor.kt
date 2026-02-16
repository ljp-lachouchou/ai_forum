package ai.ljp.designsystem.component.markdown.render.tool

import ai.ljp.designsystem.theme.AIForumTheme
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeDrawing
import androidx.compose.foundation.layout.windowInsetsBottomHeight
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp

@Composable
fun FullMarkdownEditor(initialContent: String) {
    val manager = remember { MarkdownEditorManager(initialContent) }
    val colorScheme = MaterialTheme.colorScheme
    val typography = MaterialTheme.typography

    // 复用你的 VisualTransformation
    val vt = remember(colorScheme, typography) {
        MarkdownVisualTransformation(colorScheme, typography)
    }
    val focusRequesters = remember { mutableMapOf<String, FocusRequester>() }
    Column(Modifier.fillMaxSize().imePadding()) {
        //TODO 工具栏


        // 编辑区
        LazyColumn(
            modifier = Modifier.weight(1f).padding(horizontal = 16.dp),
            contentPadding = PaddingValues(bottom = 100.dp)
        ) {
            itemsIndexed(manager.blocks, key = { _, b -> b.id }) { index, block ->
                println(block.id)
                val requester = focusRequesters.getOrPut(block.id) { FocusRequester() }
                MarkdownBlockItem(index, block, manager, vt,requester)
            }
        }

        // 保存按钮查看源码
        Button(onClick = {
            println(manager.exportMarkdown())
            println(manager.blocks.size)
        }) {
            Text("打印 Markdown 源码")
        }
        Spacer(Modifier
            .windowInsetsBottomHeight(WindowInsets.safeDrawing)
        )
    }
}
@Preview(
    showBackground = true,
    showSystemUi = true, // 必须开启
    device = "spec:width=1080px,height=2340px,navigation=buttons"
)
@Composable
fun Preview() {
    AIForumTheme {
        FullMarkdownEditor("")
    }
}