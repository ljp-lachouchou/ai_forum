package ai.ljp.data.repository

import ai.ljp.data.Syncable
import ai.ljp.data.UserSync
import ai.ljp.data.model.Bookmark
import kotlinx.datetime.Instant

interface BookmarkRepository : Syncable {

    suspend fun insertAll(bookmarks : List<Bookmark>)

    suspend fun delete(userId: String,postId : String)

    suspend fun toggleBookmark(
        userId: String,
        postId: String,
        deleted : Boolean,
        updatedAt : Instant
    )
}