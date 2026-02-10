package ai.ljp.ui

import ai.ljp.designsystem.icon.AIForumIcon
import androidx.annotation.StringRes
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ProvideTextStyle
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp

@Composable
fun AIForumToolbar(
    modifier : Modifier = Modifier,
    @StringRes titleRes : Int,
    actionIcon : (@Composable ()-> Unit)? = null,
    actionClick : () -> Unit = {},
    onBackClick : () -> Unit,
) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween,
        modifier = modifier
            .fillMaxWidth()
    ) {
        IconButton(onClick = onBackClick) {
            Icon(imageVector = AIForumIcon.ArrowBack, contentDescription = null)
        }
        ProvideTextStyle(MaterialTheme.typography.headlineMedium) {
            Text(text = stringResource(titleRes))
        }
        if (actionIcon == null) {
            Spacer(Modifier.width(1.dp))
        }else {
            IconButton(onClick = actionClick) {
                actionIcon()
            }
        }
    }

}

@Composable
fun AIForumToolbar(
    modifier : Modifier = Modifier,
    title : String,
    actionIcon : (@Composable ()-> Unit)? = null,
    actionClick : () -> Unit = {},
    onBackClick : () -> Unit,
) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween,
        modifier = modifier
            .background(color = MaterialTheme.colorScheme.primaryContainer)
            .fillMaxWidth()
    ) {
        IconButton(onClick = onBackClick) {
            Icon(imageVector = AIForumIcon.ArrowBack, contentDescription = null)
        }
        ProvideTextStyle(MaterialTheme.typography.headlineMedium) {
            Text(text = title)
        }
        if (actionIcon == null) {
            Spacer(Modifier.width(1.dp))
        }else {
            IconButton(onClick = actionClick) {
                actionIcon()
            }
        }
    }

}