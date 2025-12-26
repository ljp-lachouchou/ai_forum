package com.ljp.common.baseui.markdown.render.actual

import android.annotation.SuppressLint
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.VerticalDivider
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.TextLayoutResult
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import com.ljp.common.baseui.markdown.converter.MarkConverter
import com.ljp.common.baseui.markdown.node.MarkNode
import com.ljp.common.baseui.markdown.node.ParagraphElement
import com.ljp.common.baseui.markdown.node.TableCellData
import com.ljp.common.baseui.markdown.render.config.InlineStyleConfigBuilder
import com.ljp.common.baseui.markdown.render.devered.CheckBoxIcon
import org.commonmark.ext.gfm.tables.TablesExtension
import org.commonmark.ext.task.list.items.TaskListItemsExtension
import org.commonmark.parser.Parser

@Composable
fun MarkdownHeading(node: MarkNode.Block.Heading) {
    // 根据 level 决定样式
    val style = when (node.level) {
        1 -> MaterialTheme.typography.headlineLarge
        2 -> MaterialTheme.typography.headlineMedium
        3 -> MaterialTheme.typography.headlineSmall
        4 -> MaterialTheme.typography.titleLarge
        5 -> MaterialTheme.typography.titleMedium
        else -> MaterialTheme.typography.titleSmall
    }

    val topPadding = when (node.level) {
        1 -> 24.dp
        2 -> 18.dp
        else -> 12.dp
    }

    Column(modifier = Modifier.padding(top = topPadding, bottom = 4.dp)) {

        node.content.forEach { element ->
            when(element) {
                is ParagraphElement.TextElement -> {
                    Text(
                        text = element.content,
                        style = style.copy(
                            fontWeight = FontWeight.Bold,
                            lineHeight = style.lineHeight * 1.2
                        ),
                        color = MaterialTheme.colorScheme.onSurface
                    )
                }
                is ParagraphElement.ImageElement -> {
                    AsyncImage(
                        model = element.url,
                        contentDescription = null,
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 8.dp)
                            .clip(RoundedCornerShape(8.dp))
                            .aspectRatio(16f / 9f),
                        contentScale = ContentScale.Crop
                    )
                }
            }
        }

        if (node.level <= 2) {
            HorizontalDivider(
                modifier = Modifier.padding(top = 8.dp),
                thickness = 1.dp,
                color = MaterialTheme.colorScheme.outlineVariant
            )
        }
    }
}

@SuppressLint("ConfigurationScreenWidthHeight")
@Composable
fun MarkdownParagraph(
    node: MarkNode.Block.Paragraph,
    onLinkClick: (String) -> Unit
) {
    var layoutResult by remember { mutableStateOf<TextLayoutResult?>(null) }
    node.elements.forEach { element ->
        when(element) {
            is ParagraphElement.TextElement -> {
                Text(
                    text = element.content,
                    modifier = Modifier
                        .padding(vertical = 4.dp)
                        // 2. 核心：通过 pointerInput 手动处理点击
                        .pointerInput(Unit) {
                            detectTapGestures { pos ->
                                layoutResult?.let { result ->
                                    // 3. 将点击的坐标 (Offset) 转换为字符索引
                                    val offset = result.getOffsetForPosition(pos)

                                    // 4. 从 AnnotatedString 中提取 URL 注解
                                    element.content.getStringAnnotations(tag = "URL", start = offset, end = offset)
                                        .firstOrNull()?.let { annotation ->
                                            onLinkClick(annotation.item)
                                        }
                                }
                            }
                        },
                    style = MaterialTheme.typography.bodyLarge,
                    color = MaterialTheme.colorScheme.onSurface,
                    onTextLayout = { layoutResult = it }
                )
            }
            is ParagraphElement.ImageElement -> {
                AsyncImage(
                    model = element.url,
                    contentDescription = null,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 4.dp)
                        .clip(RoundedCornerShape(4.dp))
                        .aspectRatio(16f / 9f),
                    contentScale = ContentScale.Crop
                )
            }
        }
    }

}

@Composable
fun MarkdownBlockQuote(
    node: MarkNode.Block.BlockQuote,
    modifier: Modifier = Modifier,
    onLinkClick: (String) -> Unit
) {
    Row(
        modifier = Modifier
            .padding(vertical = 8.dp)
            .height(IntrinsicSize.Min) // 使竖线高度与内容一致
    ) {
        // 左侧竖线
        VerticalDivider(
            modifier = Modifier.padding(end = 16.dp),
            thickness = 4.dp,
            color = MaterialTheme.colorScheme.outlineVariant
        )

        Column {
            node.children.forEach { child ->
                MarkdownBlockItem(child, modifier = modifier,onLinkClick)
            }
        }
    }
}
@Composable
fun MarkdownListItem(
    node: MarkNode.Block.ListItem,
    modifier: Modifier = Modifier,
    onLinkClick: (String) -> Unit
) {
    Row(modifier = modifier) {
        if (node.isTask) {
            CheckBoxIcon(node.isCompleted,modifier = Modifier.padding(top = 3.dp, end = 6.dp))
        }else {
            Text(
                text = "• ",
                style = MaterialTheme.typography.bodyLarge,
                modifier = Modifier.padding(top = 2.dp)
            )
        }

        Column(modifier = Modifier.weight(1f)) {
            node.children.forEach { child ->
                when (child) {
                    is MarkNode.Block.BulletList, is MarkNode.Block.OrderedList -> {
                        Box(modifier = Modifier.padding(start = 16.dp)) {
                            MarkdownBlockItem(child, Modifier, onLinkClick)
                        }
                    }
                    else -> {
                        MarkdownBlockItem(child, Modifier, onLinkClick)
                    }
                }
            }
        }
    }
}
@Composable
fun MarkdownOrderedList(
    node: MarkNode.Block.OrderedList,
    modifier: Modifier = Modifier,
    onLinkClick: (String) -> Unit
) {
    Column {
        val startNumber = node.startNumber

        node.items.forEachIndexed { index, child ->
            MarkdownOrderedListItem(
                number = startNumber + index,
                node = child as MarkNode.Block.ListItem,
                onLinkClick = onLinkClick
            )
        }
    }
}
@Composable
fun MarkdownOrderedListItem(
    number: Int,
    node: MarkNode.Block.ListItem,
    onLinkClick: (String) -> Unit
) {
    Row(modifier = Modifier.padding(vertical = 4.dp)) {
        Text(
            text = "$number. ",
            style = MaterialTheme.typography.bodyLarge.copy(fontWeight = FontWeight.Bold),
            modifier = Modifier.widthIn(min = 24.dp).padding(top = 2.dp)
        )

        Column(modifier = Modifier.weight(1f)) {
            node.children.forEach { child ->
                when (child) {
                    is MarkNode.Block.BulletList, is MarkNode.Block.OrderedList -> {
                        Box(modifier = Modifier.padding(start = 16.dp)) {
                            MarkdownBlockItem(child, Modifier, onLinkClick)
                        }
                    }
                    else -> {
                        MarkdownBlockItem(child, Modifier, onLinkClick)
                    }
                }
            }
        }
    }
}
@Composable
fun MarkdownCodeBlock(node: MarkNode.Block.CodeBlock) {
    Surface(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp),
        color = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.5f),
        shape = RoundedCornerShape(8.dp)
    ) {
        Column(modifier = Modifier.padding(12.dp)) {
            if (!node.language.isNullOrBlank()) {
                Text(
                    text = node.language.uppercase(),
                    style = MaterialTheme.typography.labelSmall,
                    color = MaterialTheme.colorScheme.primary.copy(alpha = 0.7f),
                    modifier = Modifier
                        .align(Alignment.End)
                        .padding(bottom = 4.dp)
                )
            }

            Box(modifier = Modifier.horizontalScroll(rememberScrollState())) {
                Text(
                    text = node.code.trim(),
                    style = TextStyle(
                        fontFamily = FontFamily.Monospace,
                        fontSize = 14.sp,
                        lineHeight = 20.sp,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    ),
                    modifier = Modifier.padding(vertical = 4.dp)
                )
            }
        }
    }
}

@Composable
fun MarkdownTableCell(
    data: TableCellData,
    isHeader: Boolean,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
            .padding(8.dp),
        verticalArrangement = Arrangement.Center
    ) {
        data.elements.forEach { element ->
            when (element) {
                is ParagraphElement.TextElement -> {
                    Text(
                        text = element.content,
                        style = if (isHeader) {
                            MaterialTheme.typography.titleSmall
                                .copy(fontWeight = FontWeight.Bold)
                        } else {
                            MaterialTheme.typography.bodyMedium
                        },
                        // 防止长文本在单元格内不换行导致 UI 破碎
                        softWrap = true,

                    )
                }
                is ParagraphElement.ImageElement -> {
                    AsyncImage(
                        model = element.url,
                        contentDescription = null,
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 4.dp)
                            .clip(RoundedCornerShape(4.dp))
                            .aspectRatio(16f / 9f),
                        contentScale = ContentScale.Crop
                    )
                }
            }
        }
    }
}
@Composable
fun MarkdownTable(
    table: MarkNode.Block.Table,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp),
        shape = RoundedCornerShape(8.dp),
        border = BorderStroke(1.dp, MaterialTheme.colorScheme.outlineVariant),
        colors = CardDefaults.cardColors(containerColor = Color.Transparent)
    ) {
        Column(Modifier.width(IntrinsicSize.Max)) {
            Row(
                modifier = Modifier
                    .background(MaterialTheme.colorScheme.surfaceVariant)
                    .height(IntrinsicSize.Max) // 确保整行高度一致
                    .fillMaxWidth()
            ) {
                table.head.forEach { cellData ->
                    MarkdownTableCell(
                        data = cellData,
                        isHeader = true,
                        modifier = Modifier.weight(1f).fillMaxHeight()
                    )
                }
            }

            table.rows.forEachIndexed { index, rowData ->
                val backgroundColor = if (index % 2 == 0) {
                    Color.Transparent // 斑马纹效果可选
                } else {
                    MaterialTheme.colorScheme.surface.copy(alpha = 0.5f)
                }

                Row(
                    modifier = Modifier
                        .background(backgroundColor)
                        .height(IntrinsicSize.Max)
                        .fillMaxWidth()
                ) {
                    rowData.forEach { cellData ->
                        MarkdownTableCell(
                            data = cellData,
                            isHeader = false,
                            modifier = Modifier.weight(1f).fillMaxHeight()
                        )
                    }
                }
            }
        }
    }
}
@Composable
fun MarkdownBullet(
    node: MarkNode.Block.BulletList,
    modifier: Modifier = Modifier,
    onLinkClick: (String) -> Unit
) {
    Column {
        node.items.forEach { child ->
            MarkdownBlockItem(child, modifier,onLinkClick)
        }
    }
}
@Composable
fun MarkdownBlockItem(
    node: MarkNode.Block,
    modifier: Modifier = Modifier,
    onLinkClick: (String) -> Unit

) {
    when (node) {
        is MarkNode.Block.Heading -> MarkdownHeading(node)
        is MarkNode.Block.Paragraph -> MarkdownParagraph(node, onLinkClick)
        is MarkNode.Block.CodeBlock -> MarkdownCodeBlock(node)
        is MarkNode.Block.BlockQuote -> MarkdownBlockQuote(node, modifier,onLinkClick)
        is MarkNode.Block.ListItem -> MarkdownListItem(node,modifier, onLinkClick)
        is MarkNode.Block.Table -> MarkdownTable(node,modifier)
        is MarkNode.Block.ThematicBreak -> HorizontalDivider(
            modifier = Modifier.padding(vertical = 16.dp),
            color = MaterialTheme.colorScheme.outlineVariant
        )
        is MarkNode.Block.BulletList -> MarkdownBullet(node,modifier, onLinkClick)
        is MarkNode.Block.OrderedList ->
            MarkdownOrderedList(node, modifier, onLinkClick)
        else -> {}
    }
}
@Composable
fun MarkdownRenderer(
    modifier: Modifier = Modifier,
    nodes: List<MarkNode.Block>,
    onLinkClick: (String) -> Unit = {}
) {
    LazyColumn(
        modifier = modifier.fillMaxSize(),
        contentPadding = PaddingValues(16.dp)
    ) {
        items(nodes) { node ->
            MarkdownBlockItem(node, modifier,onLinkClick)
        }
    }
}
@Composable
fun MarkdownView(input: String,
                 configBlock: InlineStyleConfigBuilder.()-> Unit,

                 urlSigner:String.()-> String = {this},
                 onLinkClick: (String) -> Unit = {}) {
    val content = preprocessMarkdown(input)
    val config = InlineStyleConfigBuilder().apply(configBlock).build()
    val extensions = listOf(TablesExtension.create(), TaskListItemsExtension.create())
    val parse = Parser.builder().extensions(extensions).build()
    val rootNode = parse.parse(content)
    val visitor = MarkConverter(urlSigner,config)
    rootNode.accept(visitor)
    MarkdownRenderer(modifier = Modifier, nodes = visitor.blocks, onLinkClick = onLinkClick)
}
fun preprocessMarkdown(input: String): String {
    // 正则逻辑：找到所有以 --- 开头且上方不是空行的位置，插入一个换行
    // 注意：要避开代码块内部的情况
    return input.replace(Regex("([^\n])\n---"), "$1\n\n---")
}

