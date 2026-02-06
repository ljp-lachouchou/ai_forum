package ai.ljp.data.model

import ai.ljp.database.model.BookmarkEntity
import ai.ljp.database.model.LikeEntity
import ai.ljp.database.model.help.SyncState
import ai.ljp.network.model.SyncBookmarkItem
import ai.ljp.network.model.SyncLikeItem
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