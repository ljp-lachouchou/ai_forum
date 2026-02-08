package ai.ljp.data.repository.impl

import ai.ljp.data.SYNC_BATCH_SIZE
import ai.ljp.data.model.Follow
import ai.ljp.data.model.asDBModel
import ai.ljp.data.repository.FollowRepository
import ai.ljp.database.dao.FollowDao
import ai.ljp.network.AIForumNetworkDataSource
import ai.ljp.network.model.SyncFollowItem
import kotlinx.datetime.Instant
import javax.inject.Inject
import kotlin.collections.chunked

class OfflineFirstFollowRepository @Inject constructor(
    private val followDao: FollowDao,
    private val network : AIForumNetworkDataSource
) : FollowRepository {
    override suspend fun insertAll(follows: List<Follow>) =
        followDao.insertAll(follows.map(Follow::asDBModel))

    override suspend fun delete(userId: String, followId: String)=
        followDao.delete(userId,followId)

    override suspend fun toggleFollow(
        userId: String,
        followId: String,
        deleted: Boolean,
        updatedAt: Instant
    ) =
        followDao.toggleFollow(userId,followId,deleted,updatedAt)

    override val tableName: String
        get() = "follows"

    override suspend fun modelDeleter(ids: List<String>) =
         followDao.deleteAll(ids)

    override suspend fun modelUpdater(changedIds: List<String>) {
        changedIds.chunked(SYNC_BATCH_SIZE).forEach { ids ->
            val follows = network.syncSyncFollows(ids = ids)
                ?.map(SyncFollowItem::asDBModel) ?: return@forEach
            followDao.insertAll(follows)
        }
    }

}