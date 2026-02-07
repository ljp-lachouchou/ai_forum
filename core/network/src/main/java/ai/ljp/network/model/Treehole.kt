package ai.ljp.network.model

import kotlinx.serialization.InternalSerializationApi
import kotlinx.serialization.Serializable

@OptIn(InternalSerializationApi::class)
@Serializable
data class TreeholeCreateResponse(
    val id: String,
    val authorId: String,
    val content: String,
    val isAnonymous: Boolean,
    val createdAt: Long
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