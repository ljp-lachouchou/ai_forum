package featurei.ljp.treeholecreate.impl

import ai.ljp.designsystem.component.AIForumLoadingWheel
import ai.ljp.designsystem.component.AIForumTopAppBar
import ai.ljp.designsystem.component.DynamicAsyncImage
import ai.ljp.designsystem.icon.AIForumIcon
import ai.ljp.ui.ProfileUiState
import android.widget.Toast
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.ListItem
import androidx.compose.material3.ListItemDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ProvideTextStyle
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.ljp.model.anonymousName
import feature.ljp.treehole.api.R

@Composable
internal fun TreeholeCreateScreen(
    onBackClick : () -> Unit,
    modifier: Modifier = Modifier,
    viewModel: TreeholeCreateViewModel = hiltViewModel()
) {
    val profileUiState by viewModel.profileUiState.collectAsStateWithLifecycle()
    val content by viewModel.content.collectAsStateWithLifecycle()
    val createTreeholeUiState by viewModel.createTreeholeUiState.collectAsStateWithLifecycle()
    val isAnonymous by viewModel.isAnonymous.collectAsStateWithLifecycle()

    TreeholeCreateScreen(
        profileUiState = profileUiState,
        content = content,
        createTreeholeUiState = createTreeholeUiState,
        isAnonymous = isAnonymous,
        onBackClick = onBackClick,
        onCreateClick = viewModel::onCreateClick,
        onContentChanged = viewModel::onContentChanged,
        onAnonymousChanged = viewModel::onAnonymousChanged,
        modifier = modifier.fillMaxSize()
    )
}
@Composable
internal fun TreeholeCreateScreen(
    profileUiState : ProfileUiState,
    createTreeholeUiState : CreateTreeholeUiState,
    content : String,
    isAnonymous : Boolean,
    modifier: Modifier = Modifier,
    onBackClick : () -> Unit,
    onCreateClick : (String, Boolean) -> Unit,
    onContentChanged : (String) -> Unit,
    onAnonymousChanged : () -> Unit,
) {
    val keyboardController = LocalSoftwareKeyboardController.current
    val context = LocalContext.current
    val onCreateClickExpand = {
        keyboardController?.hide()
        onCreateClick(content,isAnonymous)
    }
    val isLoading = createTreeholeUiState is CreateTreeholeUiState.Loading
    when(createTreeholeUiState) {
        is CreateTreeholeUiState.Idle,
        is CreateTreeholeUiState.Loading-> Unit
        is CreateTreeholeUiState.Error -> {
            Toast.makeText(
                context,
                stringResource(R.string.feature_treehole_create_api_treehole_created_error),
                Toast.LENGTH_SHORT
            ).show()
        }
        is CreateTreeholeUiState.Success -> {
            Toast.makeText(
                context,
                stringResource(R.string.feature_treehole_create_api_treehole_created_success),
                Toast.LENGTH_SHORT
            ).show()
        }
    }
    Surface(
        modifier = modifier.fillMaxSize(),
        color = MaterialTheme.colorScheme.primary
    ) {
        Scaffold(
            modifier = Modifier
                .fillMaxSize()
                .background(color = Color.Transparent),
            topBar = {
                AIForumTopAppBar(
                    titleRes = R.string.feature_treehole_create_api_title,
                    navigationIcon = AIForumIcon.Close,
                    actionIcon = AIForumIcon.Publish,
                    onNavigationClick = onBackClick,
                    onActionClick = onCreateClickExpand
                )
            }
        ) { innerPadding ->
            Box(modifier = Modifier.fillMaxSize()) {
                LazyColumn(
                    modifier = Modifier.fillMaxSize().padding(innerPadding).padding(24.dp),
                    verticalArrangement = Arrangement.spacedBy(16.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    item {
                        AnonymousListItem(
                            isAnonymous = isAnonymous,
                            profileUiState = profileUiState,
                            onAnonymousChanged = onAnonymousChanged
                        )
                    }
                    item {
                        TextArea(
                            content = content,
                            onValueChange = onContentChanged
                        )
                    }
                }
                AnimatedVisibility(visible = isLoading) {
                    Box(modifier = Modifier.fillMaxWidth()) {
                        AIForumLoadingWheel(Modifier.align(Alignment.Center))
                    }
                }

            }

        }
    }
}
@Composable
private fun TextArea(
    content : String,
    onValueChange : (String) -> Unit,
) {
    val keyboardController = LocalSoftwareKeyboardController.current
    TextField(
        value = content,
        onValueChange = {
            if ("\n" in it) {
                keyboardController?.hide()
                return@TextField
            }
            onValueChange(it)
        },
        trailingIcon = {
            IconButton(onClick = {
                onValueChange("")
            }) {
                Icon(imageVector = AIForumIcon.Close, contentDescription = null)
            }
        },
        placeholder = {
            Text(
                text = stringResource(R.string.feature_treehole_create_api_treehole_content_placeholder),
                color = Color.Gray
            )
        },
        modifier = Modifier
            .fillMaxWidth()
            .heightIn(200.dp),
        minLines = 5,
        maxLines = 10,
    )
}
@Composable
private fun AnonymousListItem(
    isAnonymous: Boolean,
    profileUiState : ProfileUiState,
    onAnonymousChanged : () -> Unit,
    modifier: Modifier = Modifier
) {
    when(profileUiState) {
        is ProfileUiState.Success -> {
            val profile = profileUiState.profile
            val headlineContentText = if (isAnonymous) {
                profile.anonymousName
            }else {
                profile.userName
            }
            val supportContentText = if (isAnonymous) {
                stringResource(R.string.feature_treehole_create_api_anonymous)
            }else {
                stringResource(R.string.feature_treehole_create_api_public)
            }
            val leadingIcon : @Composable ()-> Unit = {
                if (isAnonymous) {
                    Box(
                        modifier = Modifier
                            .background(
                                color = MaterialTheme.colorScheme.surface,
                                shape = CircleShape
                            )
                    )
                }else {
                    DynamicAsyncImage(imageUrl = profile.avatarUrl, contentDescription = null,
                        modifier = Modifier.background(color = Color.Transparent, shape = CircleShape))
                }
            }
            val trailingContent : @Composable ()-> Unit = {
                Switch(
                    checked = isAnonymous,
                    onCheckedChange = {
                        onAnonymousChanged()
                    },
                )
            }
            ListItem(
                headlineContent = {
                    ProvideTextStyle(MaterialTheme.typography.headlineMedium) {
                        Text(text = headlineContentText)
                    }
                },
                leadingContent = leadingIcon,
                supportingContent = {
                    ProvideTextStyle(MaterialTheme.typography.bodySmall) {
                        Text(text = supportContentText)
                    }
                },
                trailingContent = trailingContent,
                modifier = modifier.fillMaxWidth(),
                colors = ListItemDefaults.colors(
                    containerColor = MaterialTheme.colorScheme.surface
                )
            )
        }
        is ProfileUiState.Error -> Unit
        is ProfileUiState.Loading -> {
            Box(modifier = Modifier.fillMaxWidth()) {
                AIForumLoadingWheel(Modifier.align(Alignment.Center))
            }
        }
    }
}