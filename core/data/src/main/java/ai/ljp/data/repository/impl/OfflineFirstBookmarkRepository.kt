package ai.ljp.data.repository.impl

import ai.ljp.data.SYNC_BATCH_SIZE
import ai.ljp.data.model.Bookmark
import ai.ljp.data.model.asDBModel
import ai.ljp.data.repository.BookmarkRepository
import ai.ljp.database.dao.BookmarkDao
import ai.ljp.network.AIForumNetworkDataSource
import ai.ljp.network.model.SyncBookmarkItem
import androidx.room.Transaction
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.sync.withLock
import kotlinx.datetime.Clock
import javax.inject.Inject

class OfflineFirstBookmarkRepository @Inject constructor(
    private val bookmarkDao: BookmarkDao,
    private val network: AIForumNetworkDataSource
) : BookmarkRepository {
    private val mutex = Mutex()
    override suspend fun insertAll(bookmarks: List<Bookmark>) =
        bookmarkDao.insertAll(bookmarks.asDBModel())

    override suspend fun delete(userId: String, postId: String) =
        bookmarkDao.delete(userId,postId)
    @Transaction
    override suspend fun toggleBookmark(
        userId: String,
        postId: String
    ): Boolean {
        val isBookmarked = bookmarkDao.markBookmark(postId = postId, userId = userId).first()
        val success = mutex.withLock {
            val instant = Clock.System.now()
            bookmarkDao.toggleBookmark(userId,
                postId,
                !isBookmarked,
                instant)
            val networkSuccess =  network.toggleBookmark(postId,userId)
            if (!networkSuccess) {
                bookmarkDao.toggleBookmark(userId,
                    postId,
                    isBookmarked,
                    instant)
            }
            return@withLock networkSuccess
        }
        return success

    }

    override fun markBookmark(
        postId: String,
        userId: String
    ): Flow<Boolean> =
        bookmarkDao.markBookmark(
            postId = postId,
            userId = userId
        )

    override fun getBookmarksPostId(profileId: String): List<String> =
        bookmarkDao.getBookmarksWordId(profileId)

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