package featurei.ljp.profile.impl

import ai.ljp.designsystem.component.AIForumLoadingWheel
import ai.ljp.designsystem.component.DynamicAsyncImage
import ai.ljp.ui.AIForumToolbar
import ai.ljp.ui.WordsUiState
import ai.ljp.ui.wordsItem
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ProvideTextStyle
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.paging.compose.collectAsLazyPagingItems

@Composable
internal fun ProfileScreen(
    onPostClick : (String) -> Unit,
    onBackClick : () -> Unit,
    modifier: Modifier = Modifier,
    viewModel: ProfileViewModel = hiltViewModel(),
) {
    val wordsUiState by viewModel.wordsUiState.collectAsState()
    val profileUiState by viewModel.profileUiState.collectAsState()
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
    Scaffold(
        topBar = {
            AIForumToolbar(
                titleRes =
                    feature.ljp.profile.api.R.string.feature_profile_toolbar_title,
                onBackClick = onBackClick
            )
        }
    ) { innerPadding->
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
                        feature.ljp.profile.api.R.string.
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
                    id = feature.ljp.profile.api.R.drawable.feature_profile_api_ic_detail_placeholder),
                contentDescription = null,
                tint = MaterialTheme.colorScheme.primary,
            )
            Text(
                text = stringResource(
                    id = feature.ljp.profile.api.R.string.
                    feature_profile_select_an_post),
                style = MaterialTheme.typography.titleLarge,
            )
        }
    }
}
@Composable
private fun ProfileCard(
    profileUiState: ProfileUiState,
    modifier: Modifier = Modifier
) {
    val isLoading = profileUiState is ProfileUiState.Loading
    Card(
        modifier = modifier
            .padding(16.dp),
        shape = RoundedCornerShape(20.dp),
    ) {
        when(profileUiState) {
            is ProfileUiState.Success -> {
                val profile = profileUiState.profile
                Box(modifier = Modifier.fillMaxSize()) {
                    Column(
                        modifier = Modifier
                            .align(Alignment.Center)
                    ) {
                        Spacer(Modifier.height(10.dp))
                        AvatarArea(avatarUrl = profile.avatarUrl)
                        ProvideTextStyle(MaterialTheme.typography.headlineMedium) {
                            Text(text = profile.userName)
                        }
                        ProvideTextStyle(MaterialTheme.typography.bodySmall) {
                            Text(text = profile.bio ?:"")
                        }
                    }
                }
            }
            is ProfileUiState.Error -> {}
            else -> Unit
        }
        AnimatedVisibility(visible = isLoading) {
            Box(
                modifier = Modifier.fillMaxWidth()
            ) {
                AIForumLoadingWheel(Modifier.align(Alignment.Center))
            }
        }
    }

}
@Composable
private fun AvatarArea(
    avatarUrl : String?,
) {
    Surface(
        color = Color.White,
        shape = CircleShape,
        modifier = Modifier.padding(8.dp)
    ) {
        DynamicAsyncImage(imageUrl = avatarUrl, contentDescription = null)
    }
}