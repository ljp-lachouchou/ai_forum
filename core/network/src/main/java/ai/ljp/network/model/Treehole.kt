package ai.ljp.network.model

data class TreeholeCreateResponse(
    val id: String,
    val authorId: String,
    val content: String,
    val isAnonymous: Boolean,
    val createdAt: Long
)

data class TreeholeItem(
    val id: String,
    val authorId: String,
    val content: String,
    val isAnonymous: Boolean,
    val createdAt: Long
)

data class TreeholeAiReplyResponse(
    val replyId: String,
    val treeholeId: String,
    val content: String,
    val createdAt: Long
)