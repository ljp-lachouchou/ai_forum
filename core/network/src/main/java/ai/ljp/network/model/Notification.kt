package ai.ljp.network.model

import kotlinx.serialization.InternalSerializationApi
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@OptIn(InternalSerializationApi::class)
@Serializable
data class NotificationCreateResponse(
    val id: String,
    @SerialName("user_id")
    val userId: String,
    val type: String,
    val content: String,
    @SerialName("is_read")
    val isRead: Boolean,
    @SerialName("created_at")
    val createdAt: Long
)
@OptIn(InternalSerializationApi::class)
@Serializable
data class NotificationItem(
    val id: String,
    val userId: String,
    val type: String,
    val content: String,
    val isRead: Boolean,
    val createdAt: Long
)
@OptIn(InternalSerializationApi::class)
@Serializable
data class NotificationReadResponse(
    val updatedCount: Int,
    val notificationIds: List<String>
)
@OptIn(InternalSerializationApi::class)
@Serializable
data class NotificationReadAllResponse(
    val updatedCount: Int
)

