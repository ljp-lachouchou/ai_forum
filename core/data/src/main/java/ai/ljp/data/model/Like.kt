package ai.ljp.data.model

import kotlinx.datetime.Instant

data class Like(
    val id : String,
    val postId : String,
    val userId : String,
    val createdAt : Instant
)
