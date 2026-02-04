package ai.ljp.network.model

data class LikeResponse(
    val postId: String,
    val userId: String,
    val liked: Boolean
)

data class CollectResponse(
    val postId: String,
    val userId: String,
    val collected: Boolean
)

data class UserLikeItem(
    val postId: String,
    val likedAt: Long
)