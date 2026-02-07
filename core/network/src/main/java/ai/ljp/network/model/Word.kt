package ai.ljp.network.model

import com.ljp.model.WordTag
import io.ljp.simapi.util.simApiMapOf
import kotlinx.serialization.InternalSerializationApi
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
    val wordId: String,
    val authorId: String,
    val wordUrl: String,
    val category: String,
    val tags: List<WordTag>,
    val wordName: String?,
    val status: String,
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
