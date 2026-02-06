package ai.ljp.data.repository.impl

import ai.ljp.data.SYNC_BATCH_SIZE
import ai.ljp.data.Synchronizer
import ai.ljp.data.changeSync
import ai.ljp.data.model.Follow
import ai.ljp.data.model.asDBModel
import ai.ljp.data.repository.FollowRepository
import ai.ljp.database.dao.FollowDao
import ai.ljp.database.model.BookmarkEntity
import ai.ljp.database.model.help.SyncState
import ai.ljp.database.model.roomTableName
import ai.ljp.datastore.ChangeVersion
import ai.ljp.network.AIForumNetworkDataSource
import ai.ljp.network.model.SyncFollowItem
import ai.ljp.network.model.SyncLikeItem
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

    override suspend fun syncWith(synchronizer: Synchronizer): Boolean =
        synchronizer.changeSync(
            networkEntity = BookmarkEntity::class,
            versionReader = ChangeVersion::syncVersion,
            changeFetcher = {sinceVersion ->
                network.getChangelogs(since = sinceVersion)
            },
            versionUpdater = {lastVersion ->
                ChangeVersion(syncVersion = 1L * lastVersion)
            },
            entityTagger = {entityClass ->
                entityClass.roomTableName
            },
            modelDeleter = followDao::deleteAll,
            modelUpdater = { changedIds ->//分批
                changedIds.chunked(SYNC_BATCH_SIZE).forEach { ids ->
                    val follows = network.syncSyncFollows(ids = ids)
                        ?.map(SyncFollowItem::asDBModel) ?: return@forEach
                    followDao.insertAll(follows)
                }
            }
        )

    override suspend fun updateSync(
        userId: String,
        postId: String,
        syncState: SyncState,
        updatedAt: Instant
    ) {
        TODO("Not yet implemented")
    }
}