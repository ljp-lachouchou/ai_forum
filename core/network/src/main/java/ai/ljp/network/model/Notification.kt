package ai.ljp.network.model

data class NotificationCreateResponse(
    val id: String,
    val userId: String,
    val type: String,
    val content: String,
    val isRead: Boolean,
    val createdAt: Long
)

data class NotificationItem(
    val id: String,
    val userId: String,
    val type: String,
    val content: String,
    val isRead: Boolean,
    val createdAt: Long
)
data class NotificationReadResponse(
    val updatedCount: Int,
    val notificationIds: List<String>
)

data class NotificationReadAllResponse(
    val updatedCount: Int
)

