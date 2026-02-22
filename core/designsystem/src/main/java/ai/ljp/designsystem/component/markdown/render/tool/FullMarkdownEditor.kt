package ai.ljp.designsystem.component.markdown.render.tool

import ai.ljp.designsystem.component.AIForumButton
import ai.ljp.designsystem.icon.AIForumIcon
import ai.ljp.designsystem.theme.AIForumTheme
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.navigationBars
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeDrawing
import androidx.compose.foundation.layout.windowInsetsBottomHeight
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.IconButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp

@Composable
fun FullMarkdownEditor(
    initialContent: String = "",
    manager: MarkdownEditorManager = rememberMarkdownEditorManager(initialContent),
    onImageClick : MarkdownEditorManager.() -> Unit,
) {
    val colorScheme = MaterialTheme.colorScheme
    val typography = MaterialTheme.typography

    val vt = remember(colorScheme, typography) {
        MarkdownVisualTransformation(colorScheme, typography)
    }
    val focusRequesters = remember { mutableMapOf<String, FocusRequester>() }
    Column(Modifier.fillMaxSize()) {
        Box(modifier = Modifier.weight(1f)) {
            LazyColumn(
                modifier = Modifier.fillMaxSize().padding(horizontal = 16.dp),
                contentPadding = PaddingValues(bottom = 100.dp)
            ) {
                itemsIndexed(manager.blocks, key = { _, b -> b.id }) { index, block ->
                    println(block.id)
                    val requester = focusRequesters.getOrPut(block.id) { FocusRequester() }
                    MarkdownBlockItem(index, block, manager, vt,requester)
                }
            }
            IconButton(
                onClick = {
                    onImageClick(manager)
                },
                colors = IconButtonDefaults.iconButtonColors(
//                    contentColor = MaterialTheme.colorScheme.surface,
                    containerColor = MaterialTheme.colorScheme.primary
                ),
                modifier = Modifier.align(Alignment.CenterEnd)
            ) {
                Icon(
                    imageVector = AIForumIcon.Image,
                    contentDescription = null,
                    tint = MaterialTheme.colorScheme.onPrimary
                )
            }
        }

    }
    LaunchedEffect(manager.focusedIndex) {
        if (focusRequesters.contains(manager.focusBlock.id)) {
            focusRequesters[manager.focusBlock.id]!!.requestFocus()
        }
    }
}
@Preview
@Composable
fun Preview() {
    AIForumTheme {
        FullMarkdownEditor("") {
            insertImage(focusedIndex,"https://gips1.baidu.com/it/u=2241356208,2317577872&fm=3042&app=3042&f=JPEG&wm=1,baiduai3,0,0,13,9&wmo=5,5&w=1024&h=1024")

        }
    }
}