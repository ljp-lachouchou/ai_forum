package ai.ljp.data.model

import ai.ljp.database.model.BookmarkEntity
import ai.ljp.database.model.help.SyncState
import ai.ljp.database.util.InstantConverter
import ai.ljp.network.model.SyncBookmarkItem
import ai.ljp.network.model.SyncFollowItem
import kotlinx.datetime.Instant
import kotlinx.datetime.Instant.Companion.fromEpochMilliseconds

fun SyncBookmarkItem.asDBModel() =
    BookmarkEntity(
        id = id,
        userId = userId,
        postId = postId,
        syncState = SyncState.Success,
        createdAt = createdAt.let(Instant::fromEpochMilliseconds),
    )