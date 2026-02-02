package ai.ljp.database.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.ljp.model.Notification
import kotlinx.datetime.Instant
@Entity(
    tableName = "notifications"
)
data class NotificationEntity(
    @PrimaryKey
    @ColumnInfo(name = "notificationId")
    val id : String,
    @ColumnInfo(name = "userId")
    val userId : String,
    @ColumnInfo(name = "type")
    val type : String,
    @ColumnInfo(name = "content")
    val content : String,
    @ColumnInfo(name = "refType")
    val refType : String?,
    @ColumnInfo(name = "refId")
    val refId : String?,
    @ColumnInfo(name = "read")
    val read : Boolean,
    @ColumnInfo(name = "createdAt")
    val createdAt : Instant
)

fun NotificationEntity.asExtraModel() =
    Notification(
        id = id,
        userId = userId,
        type = type,
        content = content,
        refType = refType,
        refId = refId,
        read = read,
        createdAt = createdAt
    )
