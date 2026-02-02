package ai.ljp.database.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import kotlinx.datetime.Instant
@Entity (
    tableName = "follows",
    primaryKeys = ["userId","followId"],

)
data class FollowEntity(
    @ColumnInfo(name = "userId")
    val userId : String,
    @ColumnInfo(name = "followId")
    val followId : String,
    @ColumnInfo(name = "createdAt")
    val createdAt : Instant
)
