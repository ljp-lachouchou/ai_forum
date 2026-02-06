package ai.ljp.data.model

import kotlinx.datetime.Instant

data class Follow(
    val id : String,
    val userId : String,
    val postId : String,
    val createdAt : Instant
)
