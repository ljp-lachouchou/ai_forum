package ai.ljp.data.repository.impl

import ai.ljp.data.SYNC_BATCH_SIZE
import ai.ljp.data.model.Like
import ai.ljp.data.model.asDBModel
import ai.ljp.data.repository.LikeRepository
import ai.ljp.database.dao.LikeDao
import ai.ljp.network.AIForumNetworkDataSource
import ai.ljp.network.model.SyncLikeItem
import androidx.room.Transaction
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.sync.withLock
import kotlinx.datetime.Clock
import javax.inject.Inject
import kotlin.collections.chunked

class OfflineFistLikeRepository @Inject constructor(
    private val likeDao: LikeDao,
    private val network: AIForumNetworkDataSource
) : LikeRepository {
    private val mutex = Mutex()
    override suspend fun insertAll(likes: List<Like>)  =
        likeDao.insertAll(likes.map(Like::asDBModel))

    override suspend fun delete(userId: String, postId: String) =
        likeDao.delete(userId,postId)
    override suspend fun toggleLike(
        userId: String,
        postId: String
    ) {
        val updatedAt = Clock.System.now()
        mutex.withLock {
            likeDao.toggleLike(userId, postId,  updatedAt)

            val networkSuccess = network.toggleLike(postId, userId)

            if (!networkSuccess) {
                likeDao.toggleLike(userId, postId, updatedAt)
            }
        }
    }

    override fun getLikesPostId(profileId: String): List<String> =
        likeDao.getLikesWordId(profileId)

    override fun markLike(
        postId: String,
        userId: String
    ): Flow<Boolean> =
        likeDao.markLike(postId,userId)


    override val tableName: String
        get() = "likes"

    override suspend fun modelDeleter(ids: List<String>) =
        likeDao.deleteAll(ids)

    override suspend fun modelUpdater(changedIds: List<String>) {
        changedIds.chunked(SYNC_BATCH_SIZE).forEach { ids ->
            val likes = network.syncSyncLikes(ids = ids)
                ?.map(SyncLikeItem::asDBModel) ?: return@forEach
            likeDao.insertAll(likes)
        }
    }


}