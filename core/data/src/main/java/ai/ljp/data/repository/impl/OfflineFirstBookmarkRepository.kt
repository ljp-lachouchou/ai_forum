package ai.ljp.data.repository.impl

import ai.ljp.data.Synchronizer
import ai.ljp.data.model.Bookmark
import ai.ljp.data.repository.BookmarkRepository
import ai.ljp.database.model.help.SyncState
import kotlinx.datetime.Instant

class OfflineFirstBookmarkRepository : BookmarkRepository {
    override suspend fun insertAll(bookmarks: List<Bookmark>) {
        TODO("Not yet implemented")
    }

    override suspend fun delete(userId: String, postId: String) {
        TODO("Not yet implemented")
    }

    override suspend fun toggleBookmark(
        userId: String,
        postId: String,
        deleted: Boolean,
        updatedAt: Instant
    ) {
        TODO("Not yet implemented")
    }

    override suspend fun syncWith(synchronizer: Synchronizer): Boolean {
        TODO("Not yet implemented")
    }

    override suspend fun updateSync(
        userId: String,
        postId: String,
        syncState: SyncState,
        updatedAt: Instant
    ) {
        TODO("Not yet implemented")
    }
}