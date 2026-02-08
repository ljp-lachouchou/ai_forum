package ai.ljp.network.model

import kotlinx.serialization.InternalSerializationApi
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@OptIn(InternalSerializationApi::class)
@Serializable
data class SearchResult(
    val wordId: String,
    val wordName: String,
    val score: Double
)
@OptIn(InternalSerializationApi::class)
@Serializable
data class AISearchResponse(
    @SerialName("reference_words")
    val referenceWords: List<SearchResult>,
    val answer : String
)
@OptIn(InternalSerializationApi::class)
@Serializable
data class AIReviewResponse(
    val id: String,
    val reviewed: Boolean
)
@OptIn(InternalSerializationApi::class)
@Serializable
data class AIAssistPostResponse(
    val content: String,
    val suggestions: List<String>
)