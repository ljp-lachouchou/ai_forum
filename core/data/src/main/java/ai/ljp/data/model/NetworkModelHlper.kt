package ai.ljp.data.model

import ai.ljp.database.model.BookmarkEntity
import ai.ljp.database.model.CommentEntity
import ai.ljp.database.model.FollowEntity
import ai.ljp.database.model.LikeEntity
import ai.ljp.database.model.NotificationEntity
import ai.ljp.database.model.ProfileEntity
import ai.ljp.database.model.TreeholeEntity
import ai.ljp.database.model.WordEntity
import ai.ljp.database.model.help.SyncState
import ai.ljp.network.model.AIAssistPostResponse
import ai.ljp.network.model.SyncBookmarkItem
import ai.ljp.network.model.SyncCommentItem
import ai.ljp.network.model.SyncFollowItem
import ai.ljp.network.model.SyncLikeItem
import ai.ljp.network.model.SyncNotificationItem
import ai.ljp.network.model.SyncProfileItem
import ai.ljp.network.model.SyncTreeholeItem
import ai.ljp.network.model.SyncWordItem
import com.ljp.model.Word
import kotlinx.datetime.Clock
import kotlinx.datetime.Instant
private fun Long?.toInstant() = this?.let(Instant::fromEpochMilliseconds) ?: Clock.System.now()
fun SyncBookmarkItem.asDBModel() =
    BookmarkEntity(
        id = id,
        userId = userId,
        postId = postId,
        syncState = SyncState.Success,
        createdAt = createdAt,
    )

fun SyncLikeItem.asDBModel() =
    LikeEntity(
        id = id,
        userId = userId,
        postId = postId,
        syncState = SyncState.Success,
        createdAt = createdAt,
    )

fun SyncFollowItem.asDBModel() =
    FollowEntity(
        id = id,
        userId = userId,
        followId = followId,
        syncState = SyncState.Success,
        createdAt = createdAt,
    )
fun SyncCommentItem.asDBModel() =
    CommentEntity(
        id = id,
        postId = postId,
        authorId = authorId,
        content = content,
        createdAt = createdAt,
    )

fun SyncNotificationItem.asDBModel() =
    NotificationEntity(
        id = id,
        userId = userId,
        type = type,
        content = content,
        refType = refType,
        refId = refId,
        read = read,
        createdAt = createdAt
    )

fun SyncProfileItem.asDBModel() =
    ProfileEntity(
        id = id,
        userName = username,
        avatarUrl = avatarUrl,
        bio = bio,
        role = role,
        createdAt = createdAt,
        profileCount = profileCount
    )

fun SyncTreeholeItem.asDBModel() =
    TreeholeEntity(
        id = id,
        authorId = authorId,
        content = content,
        anonymous = anonymous,
        createdAt = createdAt,
        mood = mood
    )
fun SyncWordItem.asDBModel() =
    WordEntity(
        wordId = wordId,
        authorId = authorId,
        wordTags = wordTags,
        wordUrl = wordUrl,
        category = category,
        createdAt = createdAt,
        updatedAt = updatedAt,
        wordName = wordName,
        status = status
    )
fun AIAssistPostResponse.asExtraModel() =
    AIPost(
        content = content,
        suggestions = suggestions
    )

fun SyncWordItem.asExtraModel() =
    Word(
        wordId = wordId,
        authorId = authorId,
        wordTags = wordTags,
        wordUrl = wordUrl,
        category = category,
        status = status,
        createdAt = createdAt,
        updatedAt = Clock.System.now(),
        wordName = wordName
    )