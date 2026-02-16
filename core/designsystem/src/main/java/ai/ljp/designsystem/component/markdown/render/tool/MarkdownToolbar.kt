package ai.ljp.designsystem.component.markdown.render.tool

import ai.ljp.designsystem.icon.AIForumIcon
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@Composable
fun MarkdownToolbar(
    modifier: Modifier = Modifier,
    onAction: (MarkdownStyle) -> Unit
) {
    Surface(
        color = MaterialTheme.colorScheme.surface,
        tonalElevation = 2.dp,
        modifier = modifier
            .fillMaxWidth()
            .height(48.dp)
    ) {
        LazyRow(
            verticalAlignment = Alignment.CenterVertically,
            contentPadding = PaddingValues(horizontal = 12.dp),
            horizontalArrangement = Arrangement.spacedBy(4.dp)
        ) {
            item {
                ToolbarButton(AIForumIcon.ListBulleted) {
                    onAction(MarkdownStyle.Block("- "))
                }
            }
            item {
                ToolbarButton(AIForumIcon.Bold) {
                    onAction(MarkdownStyle.Inline("**"))
                }
            }
            item {
                ToolbarButton(AIForumIcon.Italic) {
                    onAction(MarkdownStyle.Inline("*"))
                }
            }
            item {
                ToolbarButton(AIForumIcon.Quote) {
                    onAction(MarkdownStyle.Block("> "))
                }
            }
            item {
                ToolbarButton(AIForumIcon.Code) {
                    onAction(MarkdownStyle.Inline("`"))
                }
            }
            item {
                ToolbarButton(AIForumIcon.Title) {
                    onAction(MarkdownStyle.Block("## "))
                }
            }
            item {
                ToolbarButton(AIForumIcon.ListNumbered) {
                    onAction(MarkdownStyle.Block("1. "))
                }
            }
            item {
                ToolbarButton(AIForumIcon.Strikethrough) {
                    onAction(MarkdownStyle.Inline("~~"))
                }
            }

        }
    }
}