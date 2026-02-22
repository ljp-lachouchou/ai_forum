package ai.ljp.network.model

import com.ljp.model.WordTag
import kotlinx.datetime.Instant
import kotlinx.serialization.InternalSerializationApi
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@OptIn(InternalSerializationApi::class)
@Serializable
data class ChangelogItem(
    @SerialName("entity_id")
    val entityId: String,
    @SerialName("entity_type")
    val entityType: String,
    @SerialName("change_type")
    val op: String,
    @SerialName("changed_at")
    val changedAt: Instant
)
@OptIn(InternalSerializationApi::class)
@Serializable
data class GetChangeLogResponse(
    @SerialName("latest_version")
    val latestVersion : Int,
    val changes : List<ChangelogItem>
)
fun ChangelogItem.deleted() = op.trim().lowercase() == "delete"
@OptIn(InternalSerializationApi::class)
@Serializable
data class SyncWordItem(
    @SerialName("word_id")
    val wordId : String,
    @SerialName("author_id")
    val authorId : String,
    @SerialName("tags")
    val wordTags : List<WordTag>,
    @SerialName("word_url")
    val wordUrl : String,
    @SerialName("category")
    val category : String,
    @SerialName("status")
    val status : String,
    @SerialName("created_at")
    val createdAt : Instant,
    @SerialName("updated_at")
    val updatedAt : Instant,
    @SerialName("word_name")
    val wordName : String
)
@OptIn(InternalSerializationApi::class)
@Serializable
data class SyncCommentItem(
    val id : String,
    @SerialName("post_id")
    val postId : String,
    @SerialName("author_id")
    val authorId : String,
    @SerialName("content")
    val content : String,
    @SerialName("created_at")
    val createdAt : Instant
)
@OptIn(InternalSerializationApi::class)
@Serializable
data class SyncTreeholeItem(
    val id : String,
    @SerialName("author_id")
    val authorId : String,
    val content : String,
    @SerialName("is_anonymous")
    val anonymous : Boolean,
    @SerialName("created_at")
    val createdAt : Instant,
    val mood : String
)
@OptIn(InternalSerializationApi::class)
@Serializable
data class SyncNotificationItem(
    val id : String,
    @SerialName("user_id")
    val userId : String,
    val type : String,
    val content : String,
    @SerialName("ref_type")
    val refType : String?,
    @SerialName("ref_id")
    val refId : String?,
    @SerialName("is_read")
    val read : Boolean,
    @SerialName("created_at")
    val createdAt : Instant
)

@OptIn(InternalSerializationApi::class)
@Serializable
data class SyncLikeItem(
    val id : String,
    @SerialName("post_id")
    val postId : String,
    @SerialName("user_id")
    val userId : String,
    @SerialName("created_at")
    val createdAt : Instant
)
@OptIn(InternalSerializationApi::class)
@Serializable
data class SyncBookmarkItem(
    val id : String,
    @SerialName("post_id")
    val postId: String,
    @SerialName("user_id")
    val userId: String,
    @SerialName("created_at")
    val createdAt: Instant
)
@OptIn(InternalSerializationApi::class)
@Serializable
data class SyncFollowItem(
    val id : String,
    @SerialName("user_id")
    val userId: String,
    @SerialName("follow_id")
    val followId: String,
    @SerialName("created_at")
    val createdAt: Instant
)
@OptIn(InternalSerializationApi::class)
@Serializable
data class SyncProfileItem(
    val id : String,
    val username : String,
    @SerialName("avatar_url")
    val avatarUrl : String?,
    val bio : String?,
    val role : String,
    @SerialName("created_at")
    val createdAt : Instant,
    @SerialName("profile_account")
    val profileCount : String?
)


