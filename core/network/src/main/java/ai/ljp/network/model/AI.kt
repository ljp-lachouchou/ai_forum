package ai.ljp.network.model

import com.ljp.model.WordTag
import kotlinx.datetime.Instant
import kotlinx.serialization.InternalSerializationApi
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@OptIn(InternalSerializationApi::class)
@Serializable
data class SearchResult(
    @SerialName("word_id")
    val wordId: String,
    @SerialName("word_name")
    val wordName: String,
    @SerialName("author_id")
    val authorId: String,
    @SerialName("word_url")
    val wordUrl: String,
    val category: String,
    val status: String,
    val tags: List<WordTag>,
    @SerialName("created_at")
    val createdAt: Instant,
    @SerialName("updated_at")
    val updatedAt: Instant,
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