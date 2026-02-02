package ai.ljp.database.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import kotlinx.datetime.Instant

@Entity(
    tableName = "bookmarks",
    primaryKeys = ["userId","postId"]
)
data class BookmarkEntity(
    @ColumnInfo(name = "userId")
    val userId : String,
    @ColumnInfo(name = "postId")
    val postId : String,
    @ColumnInfo(name = "createdAt")
    val createdAt : Instant
)

