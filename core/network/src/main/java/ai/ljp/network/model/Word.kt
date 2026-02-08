package ai.ljp.network.model

import com.ljp.model.WordTag
import io.ljp.simapi.util.simApiMapOf
import kotlinx.serialization.InternalSerializationApi
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

fun WordUpdateRequesst.simApiMapOf() = simApiMapOf<String, Any>(
    "tags" to wordTags,
    "word_name" to wordName,
    "category" to category
)
@OptIn(InternalSerializationApi::class)
@Serializable
data class WordUpdateRequesst(
    val wordTags : List<WordTag>,
    val wordName : String,
    val category : String,
)
@OptIn(InternalSerializationApi::class)
@Serializable
data class WordDetailResponse(
    @SerialName("word_id")
    val wordId: String,
    @SerialName("author_id")
    val authorId: String,
    @SerialName("word_url")
    val wordUrl: String,
    val category: String,
    val tags: List<WordTag>,
    @SerialName("word_name")
    val wordName: String?,
    val status: String,
    @SerialName("created_at")
    val createdAt: Long
)
@OptIn(InternalSerializationApi::class)
@Serializable
data class WordFeedItem(
    val wordId: String,
    val wordName: String,
    val category: String,
    val authorId: String,
    val createdAt: Long
)
