package ai.ljp.database.dao

import ai.ljp.database.model.NotificationEntity
import androidx.paging.PagingSource
import androidx.room.Dao
import androidx.room.Query
import androidx.room.Upsert
import kotlinx.coroutines.flow.Flow

@Dao
interface NotificationDao {
    @Query("""
    UPDATE notifications
    SET `read` = 1
    WHERE `notificationId` = :id
""")
    suspend fun markAsRead(id: String)

    @Query("""
    SELECT COUNT(*) FROM notifications
    WHERE `read` = 0
""")
    fun unreadCount(): Flow<Int>

    @Query("""
        SELECT * FROM notifications
        ORDER BY createdAt DESC
    """)
    fun getNotifications(): PagingSource<Int, NotificationEntity>

    @Upsert
    suspend fun upsertNotifications(
        notifications: List<NotificationEntity>
    )
}