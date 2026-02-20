package ai.ljp.database.dao

import ai.ljp.database.model.BookmarkEntity
import ai.ljp.database.model.help.BookmarkDelete
import ai.ljp.database.model.help.SyncState
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import kotlinx.coroutines.flow.Flow
import kotlinx.datetime.Instant


@Dao
interface BookmarkDao {
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertAll(bookmarks : List<BookmarkEntity>)

    @Query(
        """
            DELETE FROM bookmarks WHERE `userId` = :userId 
            AND `postId` = :postId
        """
    )
    suspend fun delete( userId: String,postId : String)
    @Query(
        value = """
            DELETE FROM bookmarks
            WHERE `id` in (:ids)
        """,
    )
    suspend fun deleteAll(ids : List<String>)
    @Query("""
    UPDATE bookmarks 
    SET deleted = CASE WHEN deleted = 1 THEN 0 ELSE 1 END,
        createdAt = :instant
    WHERE userId = :userId AND postId = :postId
""")
    suspend fun toggleBookmark(userId: String, postId: String, instant: Instant)
    @Query (
        """
            UPDATE bookmarks 
            SET `syncState` = :syncState,
            `updatedAt` = :updatedAt 
            WHERE `userId` = :userId 
            AND `postId` = :postId
        """
    )
    suspend fun updateSync(userId: String,
                               postId: String,syncState: SyncState,
                               updatedAt : Instant)
    @Query(
        """
            SELECT EXISTS(SELECT 1 FROM bookmarks WHERE postId = :postId AND userId = :userId AND deleted = 0);

        """
    )
    fun markBookmark(
        postId: String,
        userId: String
    ): Flow<Boolean>

    @Query(
        """
            SELECT `postId` FROM bookmarks 
            WHERE `userId` = :profileId 
            ORDER BY `createdAt` DESC
        """
    )
    fun getBookmarksWordId(profileId : String) : List<String>
}