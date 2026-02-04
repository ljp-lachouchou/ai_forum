package ai.ljp.data.model

import kotlinx.datetime.Instant

data class Follow(
    val userId : String,
    val postId : String,
    val createdAt : Instant
)
