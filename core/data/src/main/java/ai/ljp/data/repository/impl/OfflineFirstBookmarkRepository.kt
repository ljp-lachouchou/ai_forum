package ai.ljp.data.repository.impl

import ai.ljp.data.SYNC_BATCH_SIZE
import ai.ljp.data.model.Bookmark
import ai.ljp.data.model.asDBModel
import ai.ljp.data.repository.BookmarkRepository
import ai.ljp.database.dao.BookmarkDao
import ai.ljp.network.AIForumNetworkDataSource
import ai.ljp.network.model.SyncBookmarkItem
import kotlinx.coroutines.delay
import kotlinx.datetime.Instant
import javax.inject.Inject

class OfflineFirstBookmarkRepository @Inject constructor(
    private val bookmarkDao: BookmarkDao,
    private val network: AIForumNetworkDataSource
) : BookmarkRepository {
    override suspend fun insertAll(bookmarks: List<Bookmark>) =
        bookmarkDao.insertAll(bookmarks.asDBModel())

    override suspend fun delete(userId: String, postId: String) =
        bookmarkDao.delete(userId,postId)

    override suspend fun toggleBookmark(
        userId: String,
        postId: String,
        deleted: Boolean,
        updatedAt: Instant
    ) {
        bookmarkDao.toggleBookmark(userId,
            postId,
            deleted,
            updatedAt)
        delay(20)
        if (!network.toggleBookmark(postId,userId)) {
            bookmarkDao.toggleBookmark(userId,
                postId,
                !deleted,
                updatedAt)
        }
    }

    override val tableName: String
        get() = "bookmarks"

    override suspend fun modelDeleter(ids: List<String>) =
        bookmarkDao.deleteAll(ids)

    override suspend fun modelUpdater(changedIds: List<String>) {
        changedIds.chunked(SYNC_BATCH_SIZE).forEach { ids ->
            val bookmarks = network.syncSyncBookmarks(ids = ids)
                ?.map(SyncBookmarkItem::asDBModel) ?: return@forEach
            bookmarkDao.insertAll(bookmarks)
        }
    }
}