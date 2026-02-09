package ai.ljp.data.repository

import ai.ljp.data.Syncable
import ai.ljp.data.model.Bookmark
import kotlinx.coroutines.flow.Flow

interface BookmarkRepository : Syncable {

    suspend fun insertAll(bookmarks : List<Bookmark>)

    suspend fun delete(userId: String,postId : String)

    suspend fun toggleBookmark(
        userId: String,
        postId: String
    )
    fun markBookmark(postId : String,userId : String) : Flow<Boolean>
}