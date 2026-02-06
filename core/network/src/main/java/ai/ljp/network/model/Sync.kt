package ai.ljp.network.model

import com.ljp.model.WordTag
import kotlinx.datetime.Instant

data class ChangelogItem(
    val id: String,
    val type: String,
    val op: String,
    val updatedAt: Long
)
data class GetChangeLogResponse(
    val latestVersion : Int,
    val changes : List<ChangelogItem>
)
fun ChangelogItem.deleted() = op.trim().lowercase() == "delete"

data class SyncWordItem(
    val wordId : String,
    val authorId : String,
    val wordTags : List<WordTag>,
    val wordUrl : String,
    val category : String,
    val status : String,
    val createdAt : Instant,
    val updatedAt : Instant,
    val wordName : String
)

data class SyncCommentItem(
    val id : String,
    val postId : String,
    val authorId : String,
    val content : String,
    val createdAt : Instant
)

data class SyncTreeholeItem(
    val id : String,
    val authorId : String?,
    val content : String,
    val anonymous : Boolean,
    val createdAt : Instant,
)

data class SyncNotificationItem(
    val id : String,
    val userId : String,
    val type : String,
    val content : String,
    val refType : String?,
    val refId : String?,
    val read : Boolean,
    val createdAt : Instant
)


data class SyncLikeItem(
    val id : String,
    val postId : String,
    val userId : String,
    val createdAt : Instant
)

data class SyncBookmarkItem(
    val id : String,
    val postId: String,
    val userId: String,
    val createdAt: Long
)

data class SyncFollowItem(
    val id : String,
    val postId: String,
    val bookmarked: Boolean,
    val updatedAt: Long
)
