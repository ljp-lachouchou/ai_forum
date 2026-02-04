package ai.ljp.network.model

data class PersonaUpdateStatsResponse(
    val userId: String,
    val category: String,
    val tags: List<String>,
    val duration: Int,
    val updatedAt: Long
)

data class PersonaUpdateAvailableResponse(
    val id: String,
    val available: Boolean
)