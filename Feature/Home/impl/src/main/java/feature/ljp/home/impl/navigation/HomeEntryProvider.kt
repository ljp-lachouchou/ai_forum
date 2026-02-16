package feature.ljp.home.impl.navigation

import ai.ljp.navigation.Navigator
import androidx.navigation3.runtime.EntryProviderScope
import androidx.navigation3.runtime.NavKey
import feature.ljp.home.api.HomeKey
import feature.ljp.home.impl.HomeScreen
import feature.ljp.post.api.navigateToPost
import feature.ljp.postcreate.api.navigateToPostCreate
import feature.ljp.profile.api.navigateToProfile
import feature.ljp.treehole.api.navigateToTreeholeCreate

fun EntryProviderScope<NavKey>.homeEntry(navigator : Navigator) {
    entry<HomeKey> {
        HomeScreen(
            onProfileClick = navigator::navigateToProfile,
            onPostClick = navigator::navigateToPost,
            onPostCreateClick = navigator::navigateToPostCreate,
            onTreeholeCreateClick = navigator::navigateToTreeholeCreate
        )
    }
}