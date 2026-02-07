package ai.ljp.network.model

import kotlinx.serialization.InternalSerializationApi
import kotlinx.serialization.Serializable

@OptIn(InternalSerializationApi::class)
@Serializable
data class LikeResponse(
    val postId: String,
    val userId: String,
    val liked: Boolean
)
@OptIn(InternalSerializationApi::class)
@Serializable
data class CollectResponse(
    val postId: String,
    val userId: String,
    val collected: Boolean
)
@OptIn(InternalSerializationApi::class)
@Serializable
data class UserLikeItem(
    val postId: String,
    val likedAt: Long
)