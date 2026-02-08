package ai.ljp.network.model

import kotlinx.serialization.InternalSerializationApi
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@OptIn(InternalSerializationApi::class)
@Serializable
data class ProfileResponse(
    val username: String?,
    @SerialName("avatar_url")
    val avatarUrl: String?,
    val bio: String?,
    val role: String,
    @SerialName("created_at")
    val createdAt: String,
    @SerialName("profile_account")
    val profileAccount: String?
)
