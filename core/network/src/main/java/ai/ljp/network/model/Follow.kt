package ai.ljp.network.model

import kotlinx.serialization.InternalSerializationApi
import kotlinx.serialization.Serializable

@OptIn(InternalSerializationApi::class)
@Serializable
data class FollowCreateResponse(
    val userId: String,
    val followId: String,
    val following: Boolean
)
@OptIn(InternalSerializationApi::class)
@Serializable
data class FollowDeleteResponse(
    val userId: String,
    val followId: String,
    val following: Boolean
)
@OptIn(InternalSerializationApi::class)
@Serializable
data class FollowItem(
    val userId: String,
    val followId: String,
    val createdAt: Long
)

