package ai.ljp.network.model

import kotlinx.datetime.Instant
import kotlinx.serialization.InternalSerializationApi
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@OptIn(InternalSerializationApi::class)
@Serializable
data class TreeholeCreateResponse(
    val id: String,
    @SerialName("author_id")
    val authorId: String,
    val content: String,
    @SerialName("is_anonymous")
    val isAnonymous: Boolean,
    @SerialName("created_at")
    val createdAt: Instant
)
@OptIn(InternalSerializationApi::class)
@Serializable
data class TreeholeItem(
    val id: String,
    val authorId: String,
    val content: String,
    val isAnonymous: Boolean,
    val createdAt: Long
)
@OptIn(InternalSerializationApi::class)
@Serializable
data class TreeholeAiReplyResponse(
    val replyId: String,
    val treeholeId: String,
    val content: String,
    val createdAt: Long
)