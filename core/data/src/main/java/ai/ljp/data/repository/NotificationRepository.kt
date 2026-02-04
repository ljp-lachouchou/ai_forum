package ai.ljp.data.repository

import ai.ljp.data.Syncable
import androidx.paging.PagingData
import com.ljp.model.Notification
import kotlinx.coroutines.flow.Flow

interface NotificationRepository : Syncable {
    suspend fun markAsRead(notificationId : String)

    suspend fun upsertNotifications(notifications : List<Notification>)

    fun getNotifications(userId : String) : Flow<PagingData<Notification>>

    fun unreadCount() : Flow<Int>


}