package ai.ljp.database.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey
import com.ljp.model.Treehole
import kotlinx.datetime.Instant
@Entity (
    tableName = "treeholes",
    foreignKeys = [
        ForeignKey(
            entity = ProfileEntity::class,
            parentColumns = ["profileId"],
            childColumns = ["authorId"],
            onDelete = ForeignKey.CASCADE
        )
    ],
    indices = [
        Index("authorId"),
    ]
)
data class TreeholeEntity(
    @PrimaryKey
    @ColumnInfo(name = "treeholeId")
    val id : String,
    @ColumnInfo(name = "authorId")
    val authorId : String?,
    @ColumnInfo(name = "content")
    val content : String,
    @ColumnInfo(name = "anonymous")
    val anonymous : Boolean,
    @ColumnInfo(name = "createdAt")
    val createdAt : Instant,
    @ColumnInfo(defaultValue = "平常")
    val mood : String = "平常",
) : BaseEntity

fun TreeholeEntity.asExtraModel() =
    Treehole(
        id = id,
        authorId = authorId,
        content = content,
        anonymous = anonymous,
        createdAt = createdAt,
        mood = mood
    )
