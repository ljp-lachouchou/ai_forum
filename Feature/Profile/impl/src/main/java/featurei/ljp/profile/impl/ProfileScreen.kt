package featurei.ljp.profile.impl

import ai.ljp.designsystem.component.AIForumLoadingWheel
import ai.ljp.designsystem.component.DynamicAsyncImage
import ai.ljp.designsystem.component.scrollbar.DraggableScrollbar
import ai.ljp.designsystem.component.scrollbar.rememberDraggableScroller
import ai.ljp.designsystem.component.scrollbar.scrollbarState
import ai.ljp.ui.AIForumToolbar
import ai.ljp.ui.ProfileCard
import ai.ljp.ui.ProfileUiState
import ai.ljp.ui.WordsUiState
import ai.ljp.ui.wordsItem
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.gestures.Orientation
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ProvideTextStyle
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.paging.compose.collectAsLazyPagingItems
import feature.ljp.profile.api.R

@Composable
internal fun ProfileScreen(
    onPostClick : (String) -> Unit,
    onBackClick : () -> Unit,
    modifier: Modifier = Modifier,
    viewModel: ProfileViewModel = hiltViewModel(),
) {
    val wordsUiState by viewModel.wordsUiState.collectAsStateWithLifecycle()
    val profileUiState by viewModel.profileUiState.collectAsStateWithLifecycle()

    ProfileScreen(
        wordsUiState = wordsUiState,
        profileUiState = profileUiState,
        onPostClick = onPostClick,
        onBackClick = onBackClick,
        modifier = modifier
    )
}
@Composable
internal fun ProfileScreen(
    wordsUiState: WordsUiState,
    profileUiState: ProfileUiState,
    onPostClick : (String) -> Unit,
    onBackClick : () -> Unit,
    modifier: Modifier = Modifier
) {
    val state = rememberLazyListState()

    val pagingItems = if (wordsUiState is WordsUiState.Success) {
        wordsUiState.wordsSource.collectAsLazyPagingItems()
    } else {
        null
    }
    val scrollState = state.scrollbarState(pagingItems?.itemCount ?: 0)
    val draggableScroller = state.rememberDraggableScroller(pagingItems?.itemCount ?: 0)
    Scaffold(
        topBar = {
            AIForumToolbar(
                titleRes =
                    R.string.feature_profile_toolbar_title,
                onBackClick = onBackClick
            )
        }
    ) { innerPadding->
        Box(
            modifier = Modifier.fillMaxSize()
        ) {
            LazyColumn(
                state = state,
                modifier = modifier
                    .fillMaxWidth()
                    .padding(innerPadding).padding(8.dp),
                verticalArrangement = Arrangement.spacedBy(5.dp)
            ) {
                item {
                    ProfileCard(
                        profileUiState = profileUiState,
                        modifier = Modifier
                            .fillMaxWidth()
                            .aspectRatio(1f)
                    )
                }
                item {
                    ProvideTextStyle(MaterialTheme.typography.headlineMedium) {
                        Text(stringResource(
                            R.string.
                            feature_profile_recent_activity_title))
                    }
                }
                when(wordsUiState) {
                    is WordsUiState.Success -> {
                        if (pagingItems != null) {
                            wordsItem(
                                wordsLazyItems = pagingItems,
                                onPostClick = onPostClick
                            )
                        }
                    }
                    is WordsUiState.Error -> {}
                    is WordsUiState.Loading -> {
                        item {
                            Box(
                                modifier = Modifier.fillMaxWidth()
                            ) {
                                AIForumLoadingWheel(Modifier.align(Alignment.Center))
                            }
                        }
                    }
                }

            }
            state.DraggableScrollbar(
                orientation = Orientation.Vertical,
                state = scrollState,
                onThumbMoved = draggableScroller,
                modifier = Modifier.align(Alignment.CenterEnd)
            )
        }


    }
}
@Composable
internal fun ProfileDetailPlaceholder(
    modifier : Modifier = Modifier
) {
    Card(
        modifier = modifier,
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
        shape = RoundedCornerShape(24.dp, 24.dp, 0.dp, 0.dp),
    ) {
        Column(
            modifier = Modifier.fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(
                20.dp,
                alignment = Alignment.CenterVertically,
            ),
        ) {
            Icon(
                painter = painterResource(
                    id = R.drawable.feature_profile_api_ic_detail_placeholder),
                contentDescription = null,
                tint = MaterialTheme.colorScheme.primary,
            )
            Text(
                text = stringResource(
                    id = R.string.
                    feature_profile_select_an_post),
                style = MaterialTheme.typography.titleLarge,
            )
        }
    }
}

