package ai.ljp.network.model

data class WordEvent(
    val id: String,
    val type: String,
    val actorId: String,
    val createdAt: Long
)