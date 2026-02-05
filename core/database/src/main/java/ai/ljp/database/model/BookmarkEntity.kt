package ai.ljp.database.model

import ai.ljp.database.model.help.SyncState
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.Index
import kotlinx.datetime.Instant

@Entity(
    tableName = "bookmarks",
    primaryKeys = ["userId","postId"],
    indices = [
        Index(value = ["postId"])
    ]
)
data class BookmarkEntity(
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
    val updatedAt : Instant
)

