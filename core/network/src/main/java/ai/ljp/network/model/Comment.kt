package ai.ljp.network.model

data class CommentCreateResponse(
    val commentId: String,
    val postId: String,
    val authorId: String,
    val content: String,
    val createdAt: Long
)

data class CommentItem(
    val commentId: String,
    val postId: String,
    val authorId: String,
    val content: String,
    val createdAt: Long
)


data class CommentDeleteResponse(
    val commentId: String,
    val deleted: Boolean
)

data class CommentSummaryResponse(
    val postId: String,
    val count: Int,
    val latestCommentId: String
)