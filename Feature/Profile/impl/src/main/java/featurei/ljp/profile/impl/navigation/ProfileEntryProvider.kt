package featurei.ljp.profile.impl.navigation

import ai.ljp.navigation.Navigator
import androidx.compose.material3.adaptive.ExperimentalMaterial3AdaptiveApi
import androidx.compose.material3.adaptive.navigation3.ListDetailSceneStrategy
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.navigation3.runtime.EntryProviderScope
import androidx.navigation3.runtime.NavKey
import feature.ljp.post.api.navigateToPost
import feature.ljp.profile.api.ProfileNavKey
import featurei.ljp.profile.impl.ProfileDetailPlaceholder
import featurei.ljp.profile.impl.ProfileScreen
import featurei.ljp.profile.impl.ProfileViewModel

@OptIn(ExperimentalMaterial3AdaptiveApi::class)
fun EntryProviderScope<NavKey>.profileEntry(navigator: Navigator) {
    entry<ProfileNavKey>(
        metadata = ListDetailSceneStrategy.listPane {
            ProfileDetailPlaceholder()
        }
    ) {key ->
        val profileId = key.profileId
        ProfileScreen(
            onPostClick = navigator::navigateToPost,
            onBackClick = { navigator.goBack() },
            viewModel = hiltViewModel<ProfileViewModel, ProfileViewModel.Factory>(
                key = profileId
            ) {factory ->
                factory.create(profileId)
            }
        )
    }
}