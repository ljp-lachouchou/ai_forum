package ai.ljp.network.model

import com.ljp.model.Word
import com.ljp.model.WordTag
import kotlinx.datetime.Instant
import kotlinx.serialization.InternalSerializationApi
import kotlinx.serialization.Serializable

@OptIn(InternalSerializationApi::class)
@Serializable
data class ChangelogItem(
    val id: String,
    val type: String,
    val op: String,
    val updatedAt: Long
)
@OptIn(InternalSerializationApi::class)
@Serializable
data class GetChangeLogResponse(
    val latestVersion : Int,
    val changes : List<ChangelogItem>
)
fun ChangelogItem.deleted() = op.trim().lowercase() == "delete"
@OptIn(InternalSerializationApi::class)
@Serializable
data class SyncWordItem(
    val wordId : String,
    val authorId : String,
    val wordTags : List<WordTag>,
    val wordUrl : String,
    val category : String,
    val status : String,
    val createdAt : Long,
    val updatedAt : Long,
    val wordName : String
)
@OptIn(InternalSerializationApi::class)
@Serializable
data class SyncCommentItem(
    val id : String,
    val postId : String,
    val authorId : String,
    val content : String,
    val createdAt : Long
)
@OptIn(InternalSerializationApi::class)
@Serializable
data class SyncTreeholeItem(
    val id : String,
    val authorId : String?,
    val content : String,
    val anonymous : Boolean,
    val createdAt : Long,
)
@OptIn(InternalSerializationApi::class)
@Serializable
data class SyncNotificationItem(
    val id : String,
    val userId : String,
    val type : String,
    val content : String,
    val refType : String?,
    val refId : String?,
    val read : Boolean,
    val createdAt : Long
)

@OptIn(InternalSerializationApi::class)
@Serializable
data class SyncLikeItem(
    val id : String,
    val postId : String,
    val userId : String,
    val createdAt : Long
)
@OptIn(InternalSerializationApi::class)
@Serializable
data class SyncBookmarkItem(
    val id : String,
    val postId: String,
    val userId: String,
    val createdAt: Long
)
@OptIn(InternalSerializationApi::class)
@Serializable
data class SyncFollowItem(
    val id : String,
    val userId: String,
    val followId: String,
    val createdAt: Long
)
@OptIn(InternalSerializationApi::class)
@Serializable
data class SyncProfileItem(
    val id : String,
    val userName : String,
    val avatarUrl : String?,
    val bio : String?,
    val role : String,
    val createdAt : Long,
    val profileCount : String?
)


