package ai.ljp.ui

import ai.ljp.designsystem.icon.AIForumIcon
import androidx.annotation.StringRes
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.FilterChip
import androidx.compose.material3.FilterChipDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp


enum class Category {
    Tech,
    Gaming
}
@Composable
fun CategoryChip(
    category: Category,
    selected : Boolean,
    modifier: Modifier = Modifier,
    onClick : () -> Unit,
) {
    FilterChip(
        selected = selected,
        onClick = onClick,
        label = {
            Text(text = category.name)
        },
        modifier = modifier,
        enabled = true,
        leadingIcon = {
            Icon(imageVector = AIForumIcon.Check, contentDescription = null)
        },
        shape = RoundedCornerShape(5.dp),
        colors = FilterChipDefaults.filterChipColors(
            labelColor = MaterialTheme.colorScheme.onPrimary,
            containerColor = MaterialTheme.colorScheme.primary,
            iconColor = MaterialTheme.colorScheme.primary
        ),
    )
}