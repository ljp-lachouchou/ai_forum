package ai.ljp.data.model

import kotlinx.datetime.Instant

data class Bookmark(
    val postId : String,
    val userId : String,
    val createdAt : Instant
)
