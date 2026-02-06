package ai.ljp.data.model

import ai.ljp.database.model.FollowEntity
import kotlinx.datetime.Instant

data class Follow(
    val id : String,
    val userId : String,
    val followId : String,
    val createdAt : Instant
)
fun Follow.asDBModel() =
    FollowEntity(
        id = id,
        userId = userId,
        followId = followId,
        createdAt = createdAt,
    )
