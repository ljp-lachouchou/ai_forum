package ai.ljp.designsystem.component

import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.material3.BottomSheetScaffold
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AIForumBottomSheetScaffold(
    sheetContent : @Composable ColumnScope.() -> Unit,
    content : @Composable (PaddingValues) -> Unit,
) {
    BottomSheetScaffold(
        sheetContent = sheetContent,
        content = content,
        sheetContainerColor = MaterialTheme.colorScheme.onSurface
    )
}