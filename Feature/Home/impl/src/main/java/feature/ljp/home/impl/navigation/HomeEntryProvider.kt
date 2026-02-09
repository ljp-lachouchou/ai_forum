package feature.ljp.home.impl.navigation

import ai.ljp.navigation.Navigator
import androidx.navigation3.runtime.EntryProviderScope
import androidx.navigation3.runtime.NavKey
import feature.ljp.home.api.HomeKey
import feature.ljp.home.impl.HomeScreen
import feature.ljp.post.api.navigateToPost
import feature.ljp.profile.api.navigateToProfile

fun EntryProviderScope<NavKey>.homeEntry(navigator : Navigator) {
    entry<HomeKey> {
        HomeScreen(
            onProfileClick = navigator::navigateToProfile,
            onPostClick = navigator::navigateToPost,
        )
    }
}