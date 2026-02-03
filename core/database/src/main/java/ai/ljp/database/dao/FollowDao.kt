package ai.ljp.database.dao

import ai.ljp.database.model.FollowEntity
import ai.ljp.database.model.help.SyncState
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import kotlinx.datetime.Instant

@Dao
interface FollowDao {
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertAll(follows : List<FollowEntity>)

    @Query(
        """
            DELETE FROM follows WHERE `userId` = :userId 
            AND `followId` = :followId
        """
    )
    suspend fun delete( userId: String,followId : String)
    @Query (
        """
            UPDATE follows 
            SET `deleted` = :deleted,
            `updatedAt` = :updatedAt 
            WHERE `userId` = :userId 
            AND `followId` = :followId
        """
    )
    suspend fun toggleFollow(userId: String,
                               followId : String,deleted : Boolean,
                               updatedAt : Instant)
    @Query (
        """
            UPDATE follows 
            SET `syncState` = :syncState,
            `updatedAt` = :updatedAt 
            WHERE `userId` = :userId 
            AND `followId` = :followId
        """
    )
    suspend fun updateSync(userId: String,
                           followId: String,syncState: SyncState,
                           updatedAt : Instant)
}