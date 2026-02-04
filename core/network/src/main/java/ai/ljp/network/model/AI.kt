package ai.ljp.network.model

data class SearchResult(
    val wordId: String,
    val wordName: String,
    val score: Double
)

data class AISearchResponse(
    val results: List<SearchResult>
)

data class AIReviewResponse(
    val id: String,
    val reviewed: Boolean
)

data class AIAssistPostResponse(
    val content: String,
    val suggestions: List<String>
)