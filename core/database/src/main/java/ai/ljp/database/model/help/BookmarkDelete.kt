package ai.ljp.database.model.help

import ai.ljp.database.model.BookmarkEntity

data class BookmarkDelete(
    val userId : String,
    val postId : String
)

fun List<BookmarkEntity>.asBookmarksDelete() =
    map {
        BookmarkDelete(
            userId = it.userId,
            postId = it.postId
        )
    }
