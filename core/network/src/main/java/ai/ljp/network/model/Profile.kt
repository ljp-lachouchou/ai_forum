package ai.ljp.network.model

import kotlinx.serialization.InternalSerializationApi
import kotlinx.serialization.Serializable

@OptIn(InternalSerializationApi::class)
@Serializable
data class ProfileResponse(
    val username: String?,
    val avatarUrl: String?,
    val bio: String?,
    val role: String,
    val createdAt: String,
    val profileAccount: String?
)
