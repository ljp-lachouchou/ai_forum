package ai.ljp.database.dao

import ai.ljp.database.model.BookmarkEntity
import ai.ljp.database.model.help.BookmarkDelete
import ai.ljp.database.model.help.SyncState
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import kotlinx.datetime.Instant


@Dao
interface BookmarkDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(bookmarks : List<BookmarkEntity>)

    @Query(
        """
            DELETE FROM bookmarks WHERE `userId` = :userId 
            AND `postId` = :postId
        """
    )
    suspend fun delete( userId: String,postId : String)
    @Query (
        """
            UPDATE bookmarks 
            SET `deleted` = :deleted,
            `updatedAt` = :updatedAt 
            WHERE `userId` = :userId 
            AND `postId` = :postId
        """
    )
    suspend fun toggleBookmark(userId: String,
                               postId: String,deleted : Boolean,
                               updatedAt : Instant)
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
}