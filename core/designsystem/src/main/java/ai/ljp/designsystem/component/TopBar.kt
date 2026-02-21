package ai.ljp.designsystem.component

import android.accessibilityservice.GestureDescription
import androidx.annotation.StringRes
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarColors
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AIForumTopAppBar(
    @StringRes titleRes :Int,
    navigationIcon : ImageVector,
    modifier: Modifier = Modifier,
    navigationIconDescription: String? = null,
    actionIcon : ImageVector? = null,
    actionIconDescription : String? = null,
    colors : TopAppBarColors = TopAppBarDefaults.topAppBarColors(),
    onNavigationClick :() -> Unit = {},
    onActionClick : () -> Unit = {}
) {
    CenterAlignedTopAppBar(
        title = { Text(text = stringResource(id = titleRes)) },
        navigationIcon = {
            IconButton(onClick =  onNavigationClick) {
                Icon(imageVector = navigationIcon,
                    contentDescription = navigationIconDescription,
                    tint = Color.Black,)
            }
        },
        actions = {
            if (actionIcon != null) {
                IconButton(onClick = onActionClick) {
                    Icon(
                        imageVector = actionIcon,
                        contentDescription = actionIconDescription,
                        tint = Color.Black
                    )
                }
            }else {
                Spacer(Modifier.height(10.dp))
            }
        },
        modifier = modifier,
        colors = colors
    )
}