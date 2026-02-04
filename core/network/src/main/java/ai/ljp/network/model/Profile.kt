package ai.ljp.network.model

data class ProfileResponse(
    val username: String?,
    val avatarUrl: String?,
    val bio: String?,
    val role: String,
    val createdAt: String,
    val profileAccount: String?
)
