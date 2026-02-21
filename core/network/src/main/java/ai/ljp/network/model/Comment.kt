package ai.ljp.network.model

import kotlinx.datetime.Instant
import kotlinx.serialization.InternalSerializationApi
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@OptIn(InternalSerializationApi::class)
@Serializable
data class CommentCreateResponse(
    @SerialName("id")
    val commentId: String,
    @SerialName("post_id")
    val postId: String,
    @SerialName("author_id")
    val authorId: String,
    val content: String,
    @SerialName("created_at")
    val createdAt: Instant
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
