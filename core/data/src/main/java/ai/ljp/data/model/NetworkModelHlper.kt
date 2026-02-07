package ai.ljp.data.model

import ai.ljp.database.model.BookmarkEntity
import ai.ljp.database.model.CommentEntity
import ai.ljp.database.model.FollowEntity
import ai.ljp.database.model.LikeEntity
import ai.ljp.database.model.NotificationEntity
import ai.ljp.database.model.ProfileEntity
import ai.ljp.database.model.TreeholeEntity
import ai.ljp.database.model.help.SyncState
import ai.ljp.network.model.SyncBookmarkItem
import ai.ljp.network.model.SyncCommentItem
import ai.ljp.network.model.SyncFollowItem
import ai.ljp.network.model.SyncLikeItem
import ai.ljp.network.model.SyncNotificationItem
import ai.ljp.network.model.SyncProfileItem
import ai.ljp.network.model.SyncTreeholeItem
import kotlinx.datetime.Clock
import kotlinx.datetime.Instant
private fun Long?.toInstant() = this?.let(Instant::fromEpochMilliseconds) ?: Clock.System.now()
fun SyncBookmarkItem.asDBModel() =
    BookmarkEntity(
        id = id,
        userId = userId,
        postId = postId,
        syncState = SyncState.Success,
        createdAt = createdAt.toInstant(),
    )

fun SyncLikeItem.asDBModel() =
    LikeEntity(
        id = id,
        userId = userId,
        postId = postId,
        syncState = SyncState.Success,
        createdAt = createdAt.toInstant(),
    )

fun SyncFollowItem.asDBModel() =
    FollowEntity(
        id = id,
        userId = userId,
        followId = followId,
        syncState = SyncState.Success,
        createdAt = createdAt.toInstant(),
    )
fun SyncCommentItem.asDBModel() =
    CommentEntity(
        id = id,
        postId = postId,
        authorId = authorId,
        content = content,
        createdAt = createdAt.toInstant(),
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
        createdAt = createdAt.toInstant()
    )

fun SyncProfileItem.asDBModel() =
    ProfileEntity(
        id = id,
        userName = userName,
        avatarUrl = avatarUrl,
        bio = bio,
        role = role,
        createdAt = createdAt.toInstant(),
        profileCount = profileCount
    )

fun SyncTreeholeItem.asDBModel() =
    TreeholeEntity(
        id = id,
        authorId = authorId,
        content = content,
        anonymous = anonymous,
        createdAt = createdAt.toInstant()
    )