package feature.ljp.me.impl.navigation

import ai.ljp.navigation.Navigator
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.ui.Modifier
import androidx.navigation3.runtime.EntryProviderScope
import androidx.navigation3.runtime.NavKey
import feature.ljp.me.api.MeNavKey
import feature.ljp.me.impl.MeScreen
import feature.ljp.post.api.navigateToPost

fun EntryProviderScope<NavKey>.meEntry(navigator: Navigator) {
    entry<MeNavKey> {
        MeScreen(
            onPostClick = navigator::navigateToPost,
            modifier = Modifier.fillMaxSize()
        )
    }
}