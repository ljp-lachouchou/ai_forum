package ai.ljp.database.model

import androidx.room.Embedded
import androidx.room.Relation
import com.ljp.model.TreeholeProfileSource

data class PopulateTreeholeEntity(
    @Embedded
    val entity : TreeholeEntity,
    @Relation(
        parentColumn = "authorId",
        entityColumn = "profileId"
    )
    val author : ProfileEntity
)
fun PopulateTreeholeEntity.asExtraModel() =
    TreeholeProfileSource(
        author = author.asExtraModel(),
        id = entity.id,
        content = entity.content,
        anonymous = entity.anonymous,
        createdAt = entity.createdAt,
        mood = entity.mood
    )