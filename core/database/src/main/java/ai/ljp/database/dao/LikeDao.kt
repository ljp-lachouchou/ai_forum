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
    @Query("""
    INSERT OR REPLACE INTO likes (id, userId, postId, deleted, createdAt, updatedAt, syncState)
    VALUES (
        -- 如果已存在则保留原 ID，否则生成新 ID
        COALESCE((SELECT id FROM likes WHERE userId = :userId AND postId = :postId), lower(hex(randomblob(16)))),
        :userId, 
        :postId, 
        COALESCE((SELECT CASE WHEN deleted = 1 THEN 0 ELSE 1 END FROM likes WHERE userId = :userId AND postId = :postId), 0), 
        COALESCE((SELECT createdAt FROM likes WHERE userId = :userId AND postId = :postId), :updatedAt),
        :updatedAt,
        0 -- 对应 SyncState.Pending
    )
""")
    suspend fun toggleLike(userId: String,
                               postId: String,
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
    @Query("""
    SELECT COUNT(*) > 0 
    FROM likes 
    WHERE postId = :postId AND userId = :userId AND deleted = 0
""")
    fun markLike(postId: String,userId: String) : Flow<Boolean>

    @Query(
        """
            SELECT `postId` FROM LIKES 
            WHERE `userId` = :profileId 
            ORDER BY `createdAt` DESC
        """
    )
    fun getLikesWordId(profileId : String) : List<String>
}