package ai.ljp.database.model

import ai.ljp.database.model.help.SyncState
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey
import kotlinx.datetime.Clock
import kotlinx.datetime.Instant
import java.util.UUID

@Entity (
    tableName = "likes",
    indices = [
        Index(value = ["postId"]),
        Index(value = ["userId"])
    ],
    primaryKeys = ["userId", "postId"]
)
data class LikeEntity(
    @ColumnInfo(name = "id")
    val id : String = UUID.randomUUID().toString(),
    @ColumnInfo(name = "userId")
    val userId : String,
    @ColumnInfo(name = "postId")
    val postId : String,
    @ColumnInfo(name = "createdAt")
    val createdAt : Instant,
    @ColumnInfo(name = "syncState")
    val syncState: SyncState = SyncState.Pending,
    @ColumnInfo(name = "deleted")
    val deleted : Boolean  = false,
    @ColumnInfo(name = "updatedAt")
    val updatedAt : kotlinx.datetime.Instant = Clock.System.now()
) : BaseEntity
