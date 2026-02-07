package ai.ljp.network.model

import kotlinx.serialization.InternalSerializationApi
import kotlinx.serialization.Serializable

@OptIn(InternalSerializationApi::class)
@Serializable
data class ReportCreateResponse(
    val reportId: String,
    val status: String
)

