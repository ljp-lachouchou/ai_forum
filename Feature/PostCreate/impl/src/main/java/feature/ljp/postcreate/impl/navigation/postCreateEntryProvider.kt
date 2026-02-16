package feature.ljp.postcreate.impl.navigation

import ai.ljp.navigation.Navigator
import androidx.navigation3.runtime.EntryProviderScope
import androidx.navigation3.runtime.NavKey
import feature.ljp.postcreate.api.PostCreateNavKey
import feature.ljp.postcreate.impl.PostCreateScreen

fun EntryProviderScope<NavKey>.postCreateEntry(navigator: Navigator) {
    entry<PostCreateNavKey> {
        PostCreateScreen(
            onBackClick = navigator::goBack
        )
    }
}