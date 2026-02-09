package ai.ljp.database.dao

import ai.ljp.database.model.LikeEntity
import ai.ljp.database.model.help.SyncState
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import kotlinx.coroutines.flow.Flow
import kotlinx.datetime.Instant

@Dao
interface LikeDao {
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertAll(likes : List<LikeEntity>)

    @Query(
        """
            DELETE FROM likes WHERE `userId` = :userId 
            AND `postId` = :postId
        """
    )
    suspend fun delete( userId: String,postId : String)
    @Query (
        """
            UPDATE likes 
            SET `deleted` = :deleted,
            `updatedAt` = :updatedAt 
            WHERE `userId` = :userId 
            AND `postId` = :postId
        """
    )
    suspend fun toggleLike(userId: String,
                               postId: String,deleted : Boolean,
                               updatedAt : Instant)
    @Query (
        """
            UPDATE likes 
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
        value = """
            DELETE FROM likes
            WHERE `id` in (:ids)
        """,
    )
    suspend fun deleteAll(ids : List<String>)
    @Query(
        """
            SELECT 1
            FROM likes
            WHERE `postId` = :postId AND `userId` = :userId AND `deleted` = 0
            LIMIT 1;

        """
    )
    fun markLike(postId: String,userId: String) : Flow<Boolean>
}