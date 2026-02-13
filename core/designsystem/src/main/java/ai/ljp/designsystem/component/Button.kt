package ai.ljp.designsystem.component

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.sizeIn
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@Composable
fun AIForumButton(
    onClick : () -> Unit,
    modifier: Modifier = Modifier,
    enable : Boolean = true,
    contentPadding : PaddingValues = ButtonDefaults.ContentPadding,
    content :  @Composable RowScope.() -> Unit,
) {
    Button(
        onClick = onClick,
        modifier = modifier,
        enabled = enable,
        colors = ButtonDefaults.buttonColors(
            containerColor = MaterialTheme.colorScheme.primary
        ),
        contentPadding = contentPadding,
        content = content
    )
}
@Composable
fun AIForumButton(
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
    text : @Composable () -> Unit,
    leadingIcon: @Composable (() -> Unit)? = null,
) {
    AIForumButton(
        onClick = onClick,
        modifier = modifier,
        enable = enabled,
        contentPadding = if (leadingIcon != null) {
            ButtonDefaults.ButtonWithIconContentPadding
        }else {
            ButtonDefaults.ContentPadding
        }
    ) {
        AIForumContent(
            text = text,
            leadingIcon = leadingIcon
        )
    }
}
@Composable
fun AIForumOutlineButton(
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
    contentPadding: PaddingValues = ButtonDefaults.ContentPadding,
    content: @Composable RowScope.() -> Unit,
) {
    OutlinedButton(
        onClick = onClick,
        modifier = modifier,
        enabled = enabled,
        colors = ButtonDefaults.outlinedButtonColors(
            MaterialTheme.colorScheme.primaryContainer
        ),
        contentPadding = contentPadding,
        border = BorderStroke(
            width =AIForumButtonDefaults.OutlinedButtonBorderWidth,
            color = if(enabled) {
                MaterialTheme.colorScheme.outline
            }else {
                MaterialTheme.colorScheme.onSurface.copy(
                    alpha = AIForumButtonDefaults.DISABLED_OUTLINED_BUTTON_BORDER_ALPHA
                )
            }
        ),
        content = content
    )
}
@Composable
fun AIForumOutlineButton(
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
    text : @Composable () -> Unit,
    leadingIcon: @Composable (() -> Unit)? = null,
) {
    AIForumOutlineButton(
        onClick = onClick,
        modifier = modifier,
        enabled = enabled,
        contentPadding = if (leadingIcon != null) {
            ButtonDefaults.ButtonWithIconContentPadding
        }else {
            ButtonDefaults.ContentPadding
        }
    ) {
        AIForumContent(
            text = text,
            leadingIcon = leadingIcon
        )
    }
}
@Composable
fun AIForumTextButton(
    onClick : () -> Unit,
    modifier: Modifier = Modifier,
    enable : Boolean = true,
    content :  @Composable RowScope.() -> Unit,
) {
    TextButton(
        onClick = onClick,
        modifier = modifier,
        enabled = enable,
        colors = ButtonDefaults.buttonColors(
            containerColor = MaterialTheme.colorScheme.primaryContainer
        ),
        content = content
    )
}
@Composable
fun AIForumTextButton(
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
    text : @Composable () -> Unit,
    leadingIcon: @Composable (() -> Unit)? = null,
) {
    AIForumTextButton(
        onClick = onClick,
        modifier = modifier,
        enable = enabled,
    ) {
        AIForumContent(
            text = text,
            leadingIcon = leadingIcon
        )
    }
}
@Composable
private fun AIForumContent(
    text : @Composable ()-> Unit,
    leadingIcon :@Composable (() -> Unit)? = null
) {
    if (leadingIcon != null) {
        Box(
            Modifier
            .sizeIn(maxHeight = ButtonDefaults.IconSize,
                maxWidth = ButtonDefaults.IconSize)
        ) {
            leadingIcon()
        }
    }
    Box(
        Modifier
            .padding(
                start = if (leadingIcon != null) {
                    ButtonDefaults.IconSpacing
                }else {0.dp}
            )
    ) {
        text()
    }
}

object AIForumButtonDefaults {
    const val DISABLED_OUTLINED_BUTTON_BORDER_ALPHA = 0.12f

    val OutlinedButtonBorderWidth = 1.dp
}