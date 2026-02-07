package ai.ljp.network.model

import kotlinx.serialization.InternalSerializationApi
import kotlinx.serialization.Serializable

@OptIn(InternalSerializationApi::class)
@Serializable
data class CommentCreateResponse(
    val commentId: String,
    val postId: String,
    val authorId: String,
    val content: String,
    val createdAt: Long
)
@OptIn(InternalSerializationApi::class)
@Serializable
data class CommentItem(
    val commentId: String,
    val postId: String,
    val authorId: String,
    val content: String,
    val createdAt: Long
)
