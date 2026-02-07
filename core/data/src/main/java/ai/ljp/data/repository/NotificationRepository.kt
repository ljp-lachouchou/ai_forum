package ai.ljp.data.repository

import ai.ljp.data.Syncable
import androidx.paging.PagingData
import com.ljp.model.Notification
import kotlinx.coroutines.flow.Flow

interface NotificationRepository : Syncable {
    suspend fun markAsRead(notificationIds : List<String>)

    suspend fun createNotification(
        type: String,
        content : String,
        refType : String?,
        refId : String?
    )

    fun getNotifications(userId : String) : Flow<PagingData<Notification>>

    fun unreadCount() : Flow<Int>


}