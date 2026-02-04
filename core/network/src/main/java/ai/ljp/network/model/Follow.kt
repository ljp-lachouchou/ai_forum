package ai.ljp.network.model

data class FollowCreateResponse(
    val userId: String,
    val followId: String,
    val following: Boolean
)

data class FollowDeleteResponse(
    val userId: String,
    val followId: String,
    val following: Boolean
)

data class FollowItem(
    val userId: String,
    val followId: String,
    val createdAt: Long
)

