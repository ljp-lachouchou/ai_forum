package ai.ljp.network.model

import kotlinx.serialization.InternalSerializationApi
import kotlinx.serialization.Serializable

@OptIn(InternalSerializationApi::class)
@Serializable
data class LikeResponse(
    val liked: Boolean
)
@OptIn(InternalSerializationApi::class)
@Serializable
data class CollectResponse(
    val collected: Boolean
)
@OptIn(InternalSerializationApi::class)
@Serializable
data class UserLikeItem(
    val postId: String,
    val likedAt: Long
)