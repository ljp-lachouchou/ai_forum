package ai.ljp.database.model

import ai.ljp.database.model.help.SyncState
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey
import kotlinx.datetime.Clock
import kotlinx.datetime.Instant
@Entity (
    tableName = "follows",

)
data class FollowEntity(
    @PrimaryKey
    @ColumnInfo(name = "id")
    val id : String,
    @ColumnInfo(name = "userId")
    val userId : String,
    @ColumnInfo(name = "followId")
    val followId : String,
    @ColumnInfo(name = "createdAt")
    val createdAt : Instant,
    @ColumnInfo(name = "syncState")
    val syncState: SyncState = SyncState.Pending,
    @ColumnInfo(name = "deleted")
    val deleted : Boolean  = false,
    @ColumnInfo(name = "updatedAt")
    val updatedAt : Instant = Clock.System.now()
) : BaseEntity
