package feature.ljp.treehole.impl.navigation

import ai.ljp.navigation.Navigator
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.ui.Modifier
import androidx.navigation3.runtime.EntryProviderScope
import androidx.navigation3.runtime.NavKey
import feature.ljp.treehole.api.TreeholeKey
import feature.ljp.treehole.impl.TreeholeScreen

fun EntryProviderScope<NavKey>.treeholeEntry(
    navigator : Navigator
) {
    entry<TreeholeKey> {
        TreeholeScreen(
            modifier = Modifier.fillMaxSize(),
        )
    }
}