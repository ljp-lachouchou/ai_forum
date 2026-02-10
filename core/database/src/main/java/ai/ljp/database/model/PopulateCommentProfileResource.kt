package ai.ljp.database.model

import androidx.room.Embedded
import androidx.room.Relation
import com.ljp.model.CommentProfileResource
import com.ljp.model.Profile

data class PopulateCommentProfileResource(
    @Embedded
    val entity : CommentEntity,
    @Relation(
        parentColumn = "authorId",
        entityColumn = "profileId"
    )
    val author : ProfileEntity
)
fun PopulateCommentProfileResource.asExtraModel() =
    CommentProfileResource(
        commentId = entity.id,
        postId = entity.postId,
        content = entity.content,
        createdAt = entity.createdAt,
        author = author.asExtraModel()
    )