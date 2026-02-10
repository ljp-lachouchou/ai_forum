package feature.ljp.post.impl.navigation

import ai.ljp.navigation.Navigator
import androidx.compose.material3.adaptive.ExperimentalMaterial3AdaptiveApi
import androidx.compose.material3.adaptive.navigation3.ListDetailSceneStrategy
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.navigation3.runtime.EntryProviderScope
import androidx.navigation3.runtime.NavKey
import feature.ljp.post.api.PostNavKey
import feature.ljp.post.impl.PostScreen
import feature.ljp.post.impl.PostViewModel

@OptIn(ExperimentalMaterial3AdaptiveApi::class)
fun EntryProviderScope<NavKey>.postEntry(navigator: Navigator) {
    entry<PostNavKey>(
        metadata = ListDetailSceneStrategy.detailPane()
    ) {key ->
        PostScreen(
            onBackClick = navigator::goBack,
            viewModel = hiltViewModel<PostViewModel, PostViewModel.Factory>(
                key = key.postId
            ) {factory ->
                factory.create(key.postId)
            }
        )
    }
}