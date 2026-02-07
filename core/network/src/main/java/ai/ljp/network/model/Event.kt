package ai.ljp.network.model

import kotlinx.serialization.InternalSerializationApi
import kotlinx.serialization.Serializable

@OptIn(InternalSerializationApi::class)
@Serializable
data class WordEvent(
    val id: String,
    val type: String,
    val actorId: String,
    val createdAt: Long
)