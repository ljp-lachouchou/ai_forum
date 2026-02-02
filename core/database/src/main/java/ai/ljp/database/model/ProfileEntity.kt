package ai.ljp.database.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.ljp.model.Profile
import kotlinx.datetime.Instant
@Entity(
    tableName = "profiles"
)
data class ProfileEntity(
    @PrimaryKey
    @ColumnInfo(name = "profileId")
    val id : String,
    @ColumnInfo(name = "userName")
    val userName : String,
    @ColumnInfo(name = "avatarUrl")
    val avatarUrl : String?,
    @ColumnInfo(name = "bio")
    val bio : String?,
    @ColumnInfo(name = "role")
    val role : String,
    @ColumnInfo(name = "createdAt")
    val createdAt : Instant,
    @ColumnInfo(name = "profileCount")
    val profileCount : String?
)

fun ProfileEntity.asExtraModel() =
    Profile(
        id = id,
        userName = userName,
        avatarUrl = avatarUrl,
        bio = bio,
        role = role,
        createdAt = createdAt,
        profileCount = profileCount
    )
