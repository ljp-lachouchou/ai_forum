package ai.ljp.designsystem.component.expandbutton

import ai.ljp.designsystem.icon.AIForumIcon
import ai.ljp.designsystem.theme.AIForumTheme
import ai.ljp.designsystem.theme.LocalTintTheme
import android.R
import androidx.annotation.StringRes
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeContentPadding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.sizeIn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.Immutable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.runtime.staticCompositionLocalOf
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.TransformOrigin
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Popup
import kotlin.math.roundToInt


@Composable
fun ExpandButton(
    modifier: Modifier = Modifier,
    state: ExpandButtonState,
    minButtonSize : Dp =30.dp,
    tintColor: Color = Color.White,
    actionIcon : ImageVector,
    popupContent : @Composable () -> Unit
) {
    val expanded = state.expanded
    val animateAngle by animateFloatAsState(
        targetValue = if (expanded) state.targetAngle else 0f,
        label = "expand button rotation"
    )
    val color by animateColorAsState(
        targetValue = if (expanded) state.expandedColor else state.actionColor
    )
    val density = LocalDensity.current
    val offsetX = with(density) {
        state.size.toPx().roundToInt()
    }
    val offsetY = with(density) {
        (state.size.toPx() * 2.3).roundToInt()
    }
    Surface(
        color = Color.Transparent, // 背景透明，只作为容器
        modifier = modifier
            .graphicsLayer(
                rotationZ = animateAngle,
                transformOrigin = TransformOrigin.Center
            )
    ) {
        Box(
            Modifier
                .sizeIn(minWidth = minButtonSize, minHeight = minButtonSize)
                .size(state.size,state.size)
                .background(
                    color = color,
                    shape = RoundedCornerShape(20.dp)
                )

                .clickable(
                    enabled = true,
                    onClick = {
                        state.rotate()
                    },
                    indication = null,
                    interactionSource = remember { MutableInteractionSource() }

                ),
            contentAlignment = Alignment.Center
        ) {
            Icon(
                modifier = Modifier.fillMaxWidth()
                    .aspectRatio(1f),
                imageVector = actionIcon,
                tint = tintColor,
                contentDescription = "expand button icon"
            )
            if (expanded) {
                Popup(
                    alignment = Alignment.TopCenter,
                    offset = IntOffset(-offsetX, - offsetY)
                ) {
                    var isVisible by remember { mutableStateOf(false) }
                    LaunchedEffect(Unit) { isVisible = true }
                    AnimatedVisibility(
                        visible = isVisible,
                        enter = fadeIn(),
                        exit = fadeOut(),
                    )  {
                        popupContent()
                    }
                }
            }
        }
    }

}

@Composable
fun PopupItem(
    @StringRes stringRes : Int,
    tailIcon : ImageVector,
    modifier : Modifier = Modifier,
    tailIconTint : Color = if (LocalTintTheme.current.iconTint == Color.Unspecified) MaterialTheme.colorScheme.onSurface else LocalTintTheme.current.iconTint,
    onClick : () -> Unit = {}
) {
    val size = LocalPopupItemSize.current.size
    Card(
        onClick = onClick,
        shape = RoundedCornerShape(20.dp),
        colors = CardDefaults.cardColors(
            MaterialTheme.colorScheme.surface
        ),
        elevation = CardDefaults.elevatedCardElevation(
            2.dp
        ),//产生阴影
        modifier = modifier.padding(2.dp)
            .height(size)
            .aspectRatio(2f)
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Center,
            modifier = Modifier.fillMaxSize()
                .padding(horizontal = 4.dp)
        ) {
            Text(text = stringResource(stringRes))
            Spacer(Modifier.safeContentPadding())
            Icon(
                imageVector = tailIcon,
                tint = tailIconTint,
                contentDescription = "PopupItem Icon"
            )
        }
    }
}

@Preview
@Composable
fun ExpandButtonPreview() {
    val state = rememberExpandButtonState()
    AIForumTheme {
        ExpandButton(
            state = state,
            actionIcon = AIForumIcon.Add,
        ) {
            PopupItem(R.string.ok, AIForumIcon.Search)
        }
    }
}
@Immutable
@JvmInline
value class PopupItemSize(val size : Dp = Dp.Unspecified)
val LocalPopupItemSize = staticCompositionLocalOf { PopupItemSize() }
@Preview
@Composable
fun ExpandButtonPreviewExpand() {
    val state = rememberExpandButtonState(true)
    AIForumTheme {
        Box(contentAlignment = Alignment.BottomEnd, modifier = Modifier.fillMaxSize()) {
            ExpandButton(
                state = state,
                actionIcon = AIForumIcon.Add,
            ) {
                CompositionLocalProvider(
                    LocalPopupItemSize provides PopupItemSize(state.size)
                ) {
                    Column {
                        Text(text = "${state.size}")
                        PopupItem(R.string.ok, AIForumIcon.Search)
                    }
                }
            }
        }
    }
}
