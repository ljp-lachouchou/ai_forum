package ai.ljp.network.model

data class ChangelogItem(
    val id: String,
    val type: String,
    val op: String,
    val updatedAt: Long
)

data class SyncWordItem(
    val wordId: String,
    val wordName: String,
    val category: String,
    val updatedAt: Long
)

data class SyncCommentItem(
    val commentId: String,
    val postId: String,
    val content: String,
    val updatedAt: Long
)

data class SyncTreeholeItem(
    val id: String,
    val content: String,
    val updatedAt: Long
)

data class SyncNotificationItem(
    val id: String,
    val type: String,
    val content: String,
    val updatedAt: Long
)


data class SyncLikeItem(
    val postId: String,
    val liked: Boolean,
    val updatedAt: Long
)

data class SyncBookmarkItem(
    val postId: String,
    val bookmarked: Boolean,
    val updatedAt: Long
)