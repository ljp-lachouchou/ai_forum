package ai.ljp.network.model

import com.ljp.model.WordTag
import io.ljp.simapi.util.simApiMapOf

fun WordUpdateRequesst.simApiMapOf() = simApiMapOf<String, Any>(
    "tags" to wordTags,
    "word_name" to wordName,
    "category" to category
)
data class WordUpdateRequesst(
    val wordTags : List<WordTag>,
    val wordName : String,
    val category : String,
)
data class WordUpdateResponse(
    val wordId: String,
    val updatedFields: Map<String, Any?>,
    val updatedAt: Long
)

data class WordSubmitResponse(
    val id: String,
    val status: String
)

data class WordPublishResponse(
    val id: String,
    val status: String
)

data class WordRejectResponse(
    val id: String,
    val status: String,
    val reason: String
)

data class WordArchiveResponse(
    val id: String,
    val status: String
)

data class WordDeleteResponse(
    val id: String,
    val deleted: Boolean
)

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
data class WordFeedItem(
    val wordId: String,
    val wordName: String,
    val category: String,
    val authorId: String,
    val createdAt: Long
)
