package ai.ljp.data.model

import ai.ljp.database.model.BookmarkEntity
import ai.ljp.database.model.CommentEntity
import ai.ljp.database.model.FollowEntity
import ai.ljp.database.model.LikeEntity
import ai.ljp.database.model.NotificationEntity
import ai.ljp.database.model.help.SyncState
import ai.ljp.network.model.SyncBookmarkItem
import ai.ljp.network.model.SyncCommentItem
import ai.ljp.network.model.SyncFollowItem
import ai.ljp.network.model.SyncLikeItem
import ai.ljp.network.model.SyncNotificationItem
import kotlinx.datetime.Instant

fun SyncBookmarkItem.asDBModel() =
    BookmarkEntity(
        id = id,
        userId = userId,
        postId = postId,
        syncState = SyncState.Success,
        createdAt = createdAt.let(Instant::fromEpochMilliseconds),
    )

fun SyncLikeItem.asDBModel() =
    LikeEntity(
        id = id,
        userId = userId,
        postId = postId,
        syncState = SyncState.Success,
        createdAt = createdAt.let(Instant::fromEpochMilliseconds),
    )

fun SyncFollowItem.asDBModel() =
    FollowEntity(
        id = id,
        userId = userId,
        followId = followId,
        syncState = SyncState.Success,
        createdAt = createdAt.let(Instant::fromEpochMilliseconds),
    )
fun SyncCommentItem.asDBModel() =
    CommentEntity(
        id = id,
        postId = postId,
        authorId = authorId,
        content = content,
        createdAt = createdAt.let(Instant::fromEpochMilliseconds),
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
        createdAt = createdAt.let(Instant::fromEpochMilliseconds)
    )