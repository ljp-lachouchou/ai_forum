package ai.ljp.data.model

import ai.ljp.database.model.LikeEntity
import kotlinx.datetime.Instant

data class Like(
    val id : String,
    val postId : String,
    val userId : String,
    val createdAt : Instant
)

fun Like.asDBModel() =
    LikeEntity(
        id = id,
        userId = userId,
        postId = postId,
        createdAt = createdAt,
    )