package ai.ljp.database.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey
import com.ljp.model.Comment
import kotlinx.datetime.Instant
@Entity(
    tableName = "comments",
    foreignKeys = [
        ForeignKey(
            entity = ProfileEntity::class,
            parentColumns = ["profileId"],
            childColumns = ["authorId"],
            onDelete = ForeignKey.CASCADE
        ),
        ForeignKey(
            entity = WordEntity::class,
            parentColumns = ["wordId"],
            childColumns = ["postId"],
            onDelete = ForeignKey.CASCADE
        )
    ],
    indices = [
        Index("follow_authorId"),
        Index("follow_postId"),
    ]
)
data class CommentEntity(
    @PrimaryKey
    @ColumnInfo(name = "commentId")
    val id : String,
    @ColumnInfo(name = "postId")
    val postId : String,
    @ColumnInfo(name = "authorId")
    val authorId : String,
    @ColumnInfo(name = "content")
    val content : String,
    @ColumnInfo(name = "createdAt")
    val createdAt : Instant
)
fun CommentEntity.asExtraModel() =
    Comment(
        id = id,
        postId = postId,
        authorId = authorId,
        content = content,
        createdAt = createdAt
    )