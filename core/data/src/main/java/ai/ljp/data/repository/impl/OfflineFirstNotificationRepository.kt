package ai.ljp.data.repository.impl

import ai.ljp.data.FeedPagingConfig
import ai.ljp.data.SYNC_BATCH_SIZE
import ai.ljp.data.model.asDBModel
import ai.ljp.data.repository.NotificationRepository
import ai.ljp.database.dao.NotificationDao
import ai.ljp.database.model.NotificationEntity
import ai.ljp.database.model.asExtraModel
import ai.ljp.datastore.AIForumPreferencesDatastore
import ai.ljp.network.AIForumNetworkDataSource
import ai.ljp.network.model.SyncNotificationItem
import androidx.paging.Pager
import androidx.paging.PagingData
import androidx.paging.map
import com.ljp.model.Notification
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map
import javax.inject.Inject
import kotlin.collections.chunked
import kotlin.collections.map

class OfflineFirstNotificationRepository @Inject constructor(
    private val notificationDao: NotificationDao,
    private val network : AIForumNetworkDataSource,
    private val preferencesDatastore: AIForumPreferencesDatastore,

) : NotificationRepository{
    override suspend fun markAsRead(notificationIds: List<String>)  {
        val userData = preferencesDatastore.userData.first()
        network.readNotification(
            userId = userData.currentUserId!!,
            notificationIds = notificationIds
        )
    }


    override suspend fun createNotification(
        type: String,
        content: String,
        refType: String?,
        refId: String?
    ) {
        val userData = preferencesDatastore.userData.first()
        network.createNotification(
            userId = userData.currentUserId!!,
            type = type,
            content = content,
            refType = refType,
            refId = refId
        )
    }


    override fun getNotifications(userId: String): Flow<PagingData<Notification>> =
        Pager(
            config = FeedPagingConfig,
            pagingSourceFactory = {
                notificationDao.getNotifications(userId)
            }
        )
            .flow
            .map { pagingData->
                pagingData.map(NotificationEntity::asExtraModel)
            }

    override fun unreadCount(): Flow<Int>  =
        notificationDao.unreadCount()



    override val tableName: String
        get() = "notifications"

    override suspend fun modelDeleter(ids: List<String>) =
        notificationDao.deleteAll(ids)

    override suspend fun modelUpdater(changedIds: List<String>) {
        changedIds.chunked(SYNC_BATCH_SIZE).forEach { ids ->
            val items = network.syncSyncNotifications(ids = ids)
                ?.map(SyncNotificationItem::asDBModel) ?: return@forEach
            notificationDao.upsertNotifications(items)
        }
    }

}