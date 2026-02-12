package ai.ljp.designsystem.component

import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.material3.BottomSheetScaffold
import androidx.compose.material3.BottomSheetScaffoldState
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.rememberBottomSheetScaffoldState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AIForumBottomSheetScaffold(
    sheetContent : @Composable ColumnScope.() -> Unit,
    content : @Composable (PaddingValues) -> Unit,
    modifier: Modifier = Modifier,
    scaffoldState : BottomSheetScaffoldState = rememberBottomSheetScaffoldState(),
) {
    BottomSheetScaffold(
        sheetContent = sheetContent,
        content = content,
        sheetContainerColor = MaterialTheme.colorScheme.onSurface,
        modifier = modifier
    )
}