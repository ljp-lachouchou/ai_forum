package ai.ljp.network.model

import kotlinx.serialization.InternalSerializationApi
import kotlinx.serialization.Serializable

@OptIn(InternalSerializationApi::class)
@Serializable
data class PersonaUpdateStatsResponse(
    val userId: String,
    val category: String,
    val tags: List<String>,
    val duration: Int,
    val updatedAt: Long
)
@OptIn(InternalSerializationApi::class)
@Serializable
data class PersonaUpdateAvailableResponse(
    val id: String,
    val available: Boolean
)