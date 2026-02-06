package ai.ljp.data.repository.impl

import ai.ljp.data.SYNC_BATCH_SIZE
import ai.ljp.data.Synchronizer
import ai.ljp.data.changeSync
import ai.ljp.data.model.Like
import ai.ljp.data.model.asDBModel
import ai.ljp.data.repository.LikeRepository
import ai.ljp.database.dao.LikeDao
import ai.ljp.database.model.BookmarkEntity
import ai.ljp.database.model.help.SyncState
import ai.ljp.database.model.roomTableName
import ai.ljp.datastore.ChangeVersion
import ai.ljp.network.AIForumNetworkDataSource
import ai.ljp.network.model.SyncLikeItem
import kotlinx.datetime.Instant
import javax.inject.Inject
import kotlin.collections.chunked

class OfflineFistLikeRepository @Inject constructor(
    private val likeDao: LikeDao,
    private val network: AIForumNetworkDataSource
) : LikeRepository {
    override suspend fun insertAll(likes: List<Like>)  =
        likeDao.insertAll(likes.map(Like::asDBModel))

    override suspend fun delete(userId: String, postId: String) =
        likeDao.delete(userId,postId)

    override suspend fun toggleLike(
        userId: String,
        postId: String,
        deleted: Boolean,
        updatedAt: Instant
    ) =
        likeDao.toggleLike(userId,
            postId,
            deleted,
            updatedAt)

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
            modelDeleter = likeDao::deleteAll,
            modelUpdater = { changedIds ->//分批
                changedIds.chunked(SYNC_BATCH_SIZE).forEach { ids ->
                    val likes = network.syncSyncLikes(ids = ids)
                        ?.map(SyncLikeItem::asDBModel) ?: return@forEach
                    likeDao.insertAll(likes)
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