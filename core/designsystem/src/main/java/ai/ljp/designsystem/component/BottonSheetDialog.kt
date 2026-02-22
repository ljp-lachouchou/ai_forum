package ai.ljp.designsystem.component

import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.SheetState
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AIForumBottomSheetScaffold(
    sheetContent : @Composable ColumnScope.() -> Unit,
    content : @Composable (PaddingValues) -> Unit,
    modifier: Modifier = Modifier,
    isVisible: Boolean = false,
    sheetState : SheetState = rememberModalBottomSheetState(),
    onDismissRequest: () -> Unit = {},
) {
    content(PaddingValues())
    if (isVisible) {
        ModalBottomSheet(
            onDismissRequest = onDismissRequest,
            sheetState = sheetState,
            containerColor = MaterialTheme.colorScheme.surface,
            modifier = modifier,
            content = sheetContent
        )
    }
}
