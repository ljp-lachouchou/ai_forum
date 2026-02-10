package feature.ljp.post.impl

import ai.ljp.designsystem.component.AIForumLoadingWheel
import ai.ljp.designsystem.component.DynamicAsyncImage
import ai.ljp.designsystem.component.DynamicContent
import ai.ljp.designsystem.icon.AIForumIcon
import ai.ljp.ui.AIForumToolbar
import ai.ljp.ui.InteractionArea
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListScope
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ProvideTextStyle
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldColors
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.key.Key
import androidx.compose.ui.input.key.key
import androidx.compose.ui.input.key.onKeyEvent
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.unit.dp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.paging.compose.LazyPagingItems
import androidx.paging.compose.collectAsLazyPagingItems
import androidx.paging.compose.itemKey
import com.ljp.model.CommentProfileResource
import feature.ljp.post.api.R
import kotlinx.coroutines.flow.StateFlow
import kotlinx.datetime.Instant

@Composable
internal fun PostScreen(
    onBackClick : () -> Unit,
    modifier: Modifier = Modifier,
    viewModel: PostViewModel = hiltViewModel()
) {
    val postUiState by viewModel.postUiState.collectAsStateWithLifecycle()
    val commentsFeedUiState by viewModel.commentsFeedUiState.collectAsStateWithLifecycle()
    val commentContent by viewModel.commentContent.collectAsStateWithLifecycle()
    PostScreen(
        postUiState = postUiState,
        commentsFeedUiState = commentsFeedUiState,
        commentContent = commentContent,
        modifier = modifier,
        onLikeClick = viewModel::toggleLike,
        onBookmarkClick = viewModel::toggleBookmark,
        onCommentContentChanged = viewModel::onCommentContentChanged,
        onCommentTriggered = viewModel::createComment,
        isLike = viewModel::isLike,
        isBookmark = viewModel::isBookmark,
        onBackClick = onBackClick
    )
}
@Composable
internal fun PostScreen(
    postUiState: PostUiState,
    commentsFeedUiState : CommentsFeedUiState,
    modifier: Modifier = Modifier,
    commentContent : String = "",
    onLikeClick : (String) -> Unit,
    onBookmarkClick : (String) -> Unit,
    onCommentContentChanged : (String) -> Unit,
    onCommentTriggered : (String, String) -> Unit,
    isLike : (String) -> StateFlow<Boolean>,
    isBookmark : (String) -> StateFlow<Boolean>,
    onBackClick : () -> Unit,

) {
    val isLoading = postUiState is PostUiState.Loading
    val pageItems = when(commentsFeedUiState) {
        is CommentsFeedUiState.Success -> commentsFeedUiState.feed.collectAsLazyPagingItems()
        else -> null
    }
    Scaffold(
        topBar = {
            AIForumToolbar(
                modifier = Modifier.fillMaxWidth(),
                title = when (postUiState) {
                    is PostUiState.Success -> {
                        stringResource(
                            R.string
                                .feature_post_api_toolbar_title,
                            postUiState.post.category

                        )
                    }
                    else -> stringResource(
                        R.string
                            .feature_post_api_loading
                    )
                },
                onBackClick = onBackClick
            )
        }
    ) { innerPadding->
        Surface(
            modifier = modifier.fillMaxSize(),
            color = MaterialTheme.colorScheme.surface
        ) {
            when(postUiState) {
                is PostUiState.Success -> {
                    Box(modifier = Modifier.fillMaxSize()) {
                        LazyColumn(
                            modifier = Modifier
                                .background(color = Color.Transparent)
                                .fillMaxSize()
                                .padding(innerPadding),
                            verticalArrangement = Arrangement.spacedBy(4.dp),
                            horizontalAlignment = Alignment.CenterHorizontally,
                        ) {
                            item {
                                ProvideTextStyle(MaterialTheme.typography.headlineLarge) {
                                    Text(text = postUiState.post.wordName)
                                }
                            }
                            item {
                                AuthorArea(
                                    avatarUrl = postUiState.post.author.avatarUrl,
                                    createdAt = postUiState.post.createdAt,
                                    username = postUiState.post.author.userName,
                                )
                            }
                            item {
                                DynamicContent(
                                    url = postUiState.post.wordUrl
                                ) {
                                    ProvideTextStyle(
                                        MaterialTheme.typography.bodyMedium
                                    ) {
                                        Text(text = it)
                                    }
                                }
                            }
                            item {
                                InteractionArea(
                                    likes = postUiState.post.likes.size,
                                    comments = postUiState.post.comments.size,
                                    bookmarks = postUiState.post.bookMarks.size,
                                    onLikeClick = onLikeClick,
                                    onBookmarkClick = onBookmarkClick,
                                    isLike = isLike,
                                    isBookmark = isBookmark,
                                    postId = postUiState.post.wordId
                                )
                            }
                            item {
                                ProvideTextStyle(MaterialTheme.typography.headlineMedium) {
                                    Text(text =
                                        stringResource(
                                            R.string.
                                            feature_post_api_community_insights

                                        )
                                    )
                                }
                            }
                            if (pageItems != null) {
                                commentsFeed(pageItems)
                            }
                        }
                        CommentSendBottombar(
                            commentContent = commentContent,
                            postId = postUiState.post.wordId,
                            onCommentContentChanged = onCommentContentChanged,
                            onCommentTriggered = onCommentTriggered
                        )
                    }
                }
                else -> Unit
            }
            AnimatedVisibility(visible = isLoading) {
                Box(Modifier.fillMaxWidth()) {
                    AIForumLoadingWheel(Modifier.align(Alignment.Center))
                }
            }

        }
    }
}
@Composable
private fun AuthorArea(
    avatarUrl : String?,
    createdAt : Instant,
    username : String,
    modifier: Modifier = Modifier
) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(
            space = 4.dp,
            alignment = Alignment.CenterHorizontally
        ),
        modifier = modifier
    ) {
        DynamicAsyncImage(
            imageUrl = avatarUrl,
            contentDescription = null,
            modifier = modifier
                .background(color = Color.Transparent,shape = CircleShape)
        )
        AuthorContent(
            createdAt = createdAt,
            username = username,
        )

    }
}

private fun LazyListScope.commentsFeed(
    pageItems : LazyPagingItems<CommentProfileResource>
) {
    items(
        count = pageItems.itemCount,
        key = pageItems.itemKey { it.commentId }
    ) { index ->
        val commentProfileRes = pageItems[index]
        if (commentProfileRes != null) {
            CommentItem(
                avatarUrl = commentProfileRes.author.avatarUrl,
                createdAt = commentProfileRes.createdAt,
                username = commentProfileRes.author.userName,
                content = commentProfileRes.content
            )
        }

    }
}
@Composable
private fun CommentItem(
    avatarUrl: String?,
    createdAt: Instant,
    username: String?,
    content : String,
    modifier: Modifier = Modifier
) {
    Row(
        horizontalArrangement = Arrangement.spacedBy(8.dp),
        modifier = modifier.fillMaxSize(),
        verticalAlignment = Alignment.CenterVertically
    ) {
        DynamicAsyncImage(
            imageUrl = avatarUrl,
            contentDescription = null,
            modifier = modifier
                .background(color = Color.Transparent,shape = CircleShape)
        )
        CommentContent(username = username,
            createdAt = createdAt,
            content = content)
    }
}
@Composable
private fun CommentContent(
    username: String?,
    createdAt: Instant,
    content: String,
    modifier: Modifier = Modifier
) {
    Card(
        shape = RoundedCornerShape(
            topEnd = 20.dp,
            topStart = 0.dp,
            bottomEnd = 20.dp,
            bottomStart = 20.dp
        ),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.primaryContainer
        ),
        modifier = modifier.fillMaxWidth()
    ) {
        Column(
            modifier = Modifier.fillMaxSize(),
            verticalArrangement = Arrangement.spacedBy(2.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                ProvideTextStyle(
                    MaterialTheme.typography.headlineSmall
                ) {
                    Text(text = username ?:
                    stringResource(
                        R.string.feature_post_api_username
                    ))
                }
                ProvideTextStyle(
                    MaterialTheme.typography.displaySmall
                ) {
                    Text(text = createdAt.toString())
                }
            }
            ProvideTextStyle(MaterialTheme.typography.bodySmall)  {
                Text(text = content)
            }
        }
    }
}
@Composable
private fun CommentSendBottombar(
    commentContent: String,
    postId: String,
    onCommentContentChanged :(String) -> Unit,
    onCommentTriggered : (String,String) -> Unit
) {
    val focusRequester = remember { FocusRequester() }
    val keyboardController = LocalSoftwareKeyboardController.current
    Row(
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(4.dp),
        modifier = Modifier.fillMaxWidth()
    ) {
        SendCommentTextField(
            commentContent = commentContent,
            postId = postId,
            onCommentTriggered = onCommentTriggered,
            focusRequester = focusRequester,
            onCommentContentChanged = onCommentContentChanged
        )
        IconButton(
            onClick = {
                keyboardController?.hide()
                onCommentTriggered(postId,commentContent)
            },
            shape = CircleShape
        ) {
            Icon(
                imageVector = AIForumIcon.Send,
                contentDescription = null,
                tint = MaterialTheme.colorScheme.onSurface
            )
        }
    }
}
@Composable
private fun SendCommentTextField(
    commentContent : String,
    postId : String,
    focusRequester : FocusRequester,
    onCommentContentChanged :(String) -> Unit,
    onCommentTriggered : (String,String) -> Unit
) {
    val keyboardController = LocalSoftwareKeyboardController.current
    val onExplicitlyTriggered = {
        keyboardController?.hide()
        onCommentTriggered(postId,commentContent)
    }
    TextField(
        value = commentContent,
        onValueChange = {
            if ("\n" !in it) {
                onCommentContentChanged(it)
            }
        },
        colors = TextFieldDefaults.colors(
            focusedIndicatorColor = Color.Transparent,
            unfocusedIndicatorColor = Color.Transparent,
            disabledIndicatorColor = Color.Transparent,
        ),
        trailingIcon = {
            if (commentContent.isNotEmpty()) {
                IconButton(
                    onClick = {
                        onCommentContentChanged("")
                    }
                ) {
                    Icon(
                        imageVector = AIForumIcon.Close,
                        contentDescription = null,
                        tint = MaterialTheme.colorScheme.onSurface
                    )
                }
            }
        },
        singleLine = true,
        maxLines = 1,
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp)
            .focusRequester(focusRequester)
            .onKeyEvent {
                if (it.key == Key.Enter) {
                    if (commentContent.isBlank()) return@onKeyEvent false
                    onExplicitlyTriggered()
                    true
                    true
                } else {
                    false
                }
            },
        shape = RoundedCornerShape(32.dp),
        keyboardActions = KeyboardActions(
            onSend = {
                if (commentContent.isBlank()) return@KeyboardActions
                onExplicitlyTriggered()
            }
        ),
        keyboardOptions = KeyboardOptions(imeAction = ImeAction.Send)

    )
    LaunchedEffect(Unit) {
        focusRequester.requestFocus()
    }
}
@Composable
private fun AuthorContent(
    createdAt : Instant,
    username : String,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier,
        verticalArrangement = Arrangement.SpaceBetween,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        ProvideTextStyle(MaterialTheme.typography.headlineSmall) {
            Text(text = username)
        }
        ProvideTextStyle(MaterialTheme.typography.bodySmall) {
            Text(text = createdAt.toString())
        }
    }
}
