package com.ljp.common.baseui.markdown.render.actual

import android.R
import android.util.Log
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
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
import androidx.compose.foundation.text.InlineTextContent
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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.Placeholder
import androidx.compose.ui.text.PlaceholderVerticalAlign
import androidx.compose.ui.text.TextLayoutResult
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import com.ljp.common.baseui.markdown.converter.MarkConverter
import com.ljp.common.baseui.markdown.node.MarkNode
import com.ljp.common.baseui.markdown.node.TableCellData
import com.ljp.common.baseui.markdown.render.config.InlineStyleConfigBuilder
import org.commonmark.ext.gfm.tables.TablesExtension
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
        Text(
            text = node.content,
            style = style.copy(
                fontWeight = FontWeight.Bold,
                // M3 的标题行间距通常较紧凑，可以根据需要微调
                lineHeight = style.lineHeight * 1.2
            ),
            color = MaterialTheme.colorScheme.onSurface
        )

        if (node.level <= 2) {
            HorizontalDivider(
                modifier = Modifier.padding(top = 4.dp),
                thickness = 1.dp,
                color = MaterialTheme.colorScheme.outlineVariant
            )
        }
    }
}

@Composable
fun MarkdownParagraph(
    node: MarkNode.Block.Paragraph,
    onLinkClick: (String) -> Unit
) {
    // 1. 存储布局结果，用于将点击坐标转换为字符索引
    var layoutResult by remember { mutableStateOf<TextLayoutResult?>(null) }

    // 处理行内图片占位符（Text 支持这个属性）
    val inlineContent = node.imageMap.map { (id, url) ->
        id to InlineTextContent(
            Placeholder(125.sp, 125.sp, PlaceholderVerticalAlign.Center)
        ) {
            Log.e("sadsa",url)
            AsyncImage(model = url,
                contentDescription = null,
                contentScale = ContentScale.Crop,
                placeholder = painterResource(R.drawable.star_on))
        }
    }.toMap()

    Text(
        text = node.content,
        modifier = Modifier
            .padding(vertical = 4.dp)
            // 2. 核心：通过 pointerInput 手动处理点击
            .pointerInput(Unit) {
                detectTapGestures { pos ->
                    layoutResult?.let { result ->
                        // 3. 将点击的坐标 (Offset) 转换为字符索引
                        val offset = result.getOffsetForPosition(pos)

                        // 4. 从 AnnotatedString 中提取 URL 注解
                        node.content.getStringAnnotations(tag = "URL", start = offset, end = offset)
                            .firstOrNull()?.let { annotation ->
                                onLinkClick(annotation.item)
                            }
                    }
                }
            },
        style = MaterialTheme.typography.bodyLarge,
        color = MaterialTheme.colorScheme.onSurface,
        inlineContent = inlineContent,
        onTextLayout = { layoutResult = it }
    )
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
    Row(modifier = Modifier.padding(vertical = 2.dp)) {
        // 渲染列表符号（例如：• ）
        Text(
            text = "• ",
            style = MaterialTheme.typography.bodyLarge,
            modifier = Modifier.padding(end = 8.dp)
        )

        Column {
            node.children.forEach { child ->
                MarkdownBlockItem(child, modifier,onLinkClick)
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
    val inlineContent = remember(data) {
        data.images.mapValues { (_, url) ->
            InlineTextContent(
                Placeholder(100.sp, 100.sp, PlaceholderVerticalAlign.Center)
            ) {
                AsyncImage(
                    model = url,
                    contentDescription = null,
                    modifier = Modifier.fillMaxSize(),
                    contentScale = ContentScale.Crop,
                    placeholder = painterResource(R.drawable.star_on)
                )
            }
        }
    }

    Box(
        modifier = modifier
            .border(0.5.dp, MaterialTheme.colorScheme.outlineVariant)
            .padding(12.dp),
        contentAlignment = if (isHeader) Alignment.Center else Alignment.CenterStart
    ) {
        Text(
            text = data.text,
            inlineContent = inlineContent,
            style = if (isHeader) {
                MaterialTheme.typography.titleSmall.copy(fontWeight = FontWeight.Bold)
            } else {
                MaterialTheme.typography.bodyMedium
            },
            color = if (isHeader) MaterialTheme.colorScheme.onSurface else MaterialTheme.colorScheme.onSurfaceVariant
        )
    }
}
@Composable
fun MarkdownTable(
    table: MarkNode.Block.Table,
    modifier: Modifier = Modifier
) {
    val scrollState = rememberScrollState()

    // 计算列数
    val columnCount = table.head.size.coerceAtLeast(
        table.rows.firstOrNull()?.size ?: 0
    )

    Card(
        modifier = modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp),
        shape = RoundedCornerShape(8.dp),
        border = BorderStroke(1.dp, MaterialTheme.colorScheme.outlineVariant),
        colors = CardDefaults.cardColors(containerColor = Color.Transparent)
    ) {
        Column(Modifier.width(IntrinsicSize.Max)) {
            // 1. 渲染表头 (Table Head)
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

            // 2. 渲染数据行 (Table Rows)
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
fun MarkdownView(content: String,
                 configBlock: InlineStyleConfigBuilder.()-> Unit,
                 modifier: Modifier = Modifier,

                 urlSigner:String.()-> String = {this},
                 onLinkClick: (String) -> Unit = {}) {
    val config = InlineStyleConfigBuilder().apply(configBlock).build()
    val extensions = listOf(TablesExtension.create())
    val parse = Parser.builder().extensions(extensions).build()
    val rootNode = parse.parse(content)
    val visitor = MarkConverter(urlSigner,config)
    rootNode.accept(visitor)
    MarkdownRenderer(modifier = modifier, nodes = visitor.blocks, onLinkClick = onLinkClick)
}

