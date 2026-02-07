package ai.ljp.data.repository.impl

import ai.ljp.data.FeedPagingConfig
import ai.ljp.data.SYNC_BATCH_SIZE
import ai.ljp.data.model.asDBModel
import ai.ljp.data.repository.TreeholeRepository
import ai.ljp.database.dao.TreeholeDao
import ai.ljp.database.model.TreeholeEntity
import ai.ljp.database.model.asExtraModel
import ai.ljp.datastore.AIForumPreferencesDatastore
import ai.ljp.network.AIForumNetworkDataSource
import ai.ljp.network.model.SyncTreeholeItem
import androidx.paging.Pager
import androidx.paging.PagingData
import androidx.paging.map
import com.ljp.model.Treehole
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map
import javax.inject.Inject
import kotlin.collections.chunked

class OfflineFirstTreeholeRepository @Inject constructor(
    private val network: AIForumNetworkDataSource,
    private val preferencesDatastore: AIForumPreferencesDatastore,
    private val treeholeDao: TreeholeDao
) : TreeholeRepository{
    override suspend fun createTreehole(
        content: String,
        isAnonymous: Boolean
    ) {
        val userData = preferencesDatastore.userData.first()
        network.createTreehole(
            authorId = userData.currentUserId,
            content = content,
            isAnonymous = isAnonymous
        )
    }

    override fun getTreeholes(): Flow<PagingData<Treehole>> =
        Pager(
            config = FeedPagingConfig,
            pagingSourceFactory = {
                treeholeDao.getTreeholes()
            }
        )
            .flow
            .map { pagingData->
                pagingData.map(TreeholeEntity::asExtraModel)
            }

    override val tableName: String
        get() = "treeholes"

    override suspend fun modelDeleter(ids: List<String>) =
        treeholeDao.deleteAll(ids)

    override suspend fun modelUpdater(changedIds: List<String>) {
        changedIds.chunked(SYNC_BATCH_SIZE).forEach { ids ->
            val items = network.syncSyncTreeholes(ids = ids)
                ?.map(SyncTreeholeItem::asDBModel) ?: return@forEach
            treeholeDao.upsertTreeholes(items)
        }
    }
}