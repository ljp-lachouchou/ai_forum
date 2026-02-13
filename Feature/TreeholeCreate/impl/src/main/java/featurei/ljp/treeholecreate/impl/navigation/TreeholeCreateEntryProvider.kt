package featurei.ljp.treeholecreate.impl.navigation

import ai.ljp.navigation.Navigator
import androidx.navigation3.runtime.EntryProviderScope
import androidx.navigation3.runtime.NavKey
import feature.ljp.treehole.api.TreeholeCreateKey
import featurei.ljp.treeholecreate.impl.TreeholeCreateScreen

fun EntryProviderScope<NavKey>.treeholeCreateEntry(navigator: Navigator) {
    entry<TreeholeCreateKey> {
        TreeholeCreateScreen(
            onBackClick = navigator::goBack
        )
    }
}