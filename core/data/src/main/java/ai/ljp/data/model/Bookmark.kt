package ai.ljp.data.model

import ai.ljp.database.model.BookmarkEntity
import kotlinx.datetime.Instant

data class Bookmark(
    val id : String,
    val postId : String,
    val userId : String,
    val createdAt : Instant
)
fun List<Bookmark>.asDBModel() =
    map {
        BookmarkEntity(
            id = it.id,
            userId = it.userId,
            postId = it.postId,
            createdAt = it.createdAt,
        )
    }