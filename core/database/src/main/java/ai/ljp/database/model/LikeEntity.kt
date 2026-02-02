package ai.ljp.database.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import java.time.Instant
@Entity (
    tableName = "likes",
    primaryKeys = ["userId","postId"],
)
data class LikeEntity(
    @ColumnInfo(name = "userId")
    val userId : String,
    @ColumnInfo(name = "postId")
    val postId : String,
    @ColumnInfo(name = "createdAt")
    val createdAt : Instant
)
