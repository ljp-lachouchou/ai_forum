package feature.ljp.search.impl.navigation

import ai.ljp.navigation.Navigator
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.ui.Modifier
import androidx.navigation3.runtime.EntryProviderScope
import androidx.navigation3.runtime.NavKey
import feature.ljp.post.api.navigateToPost
import feature.ljp.search.api.SearchNavKey
import feature.ljp.search.impl.SearchScreen

fun EntryProviderScope<NavKey>.searchEntry(navigator: Navigator) {
    entry<SearchNavKey> {
        SearchScreen(
            onBackClick = navigator::goBack,
            onPostClick = navigator::navigateToPost,
            modifier = Modifier.fillMaxSize()
        )
    }
}