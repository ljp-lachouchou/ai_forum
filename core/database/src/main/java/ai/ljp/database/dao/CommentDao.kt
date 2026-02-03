package ai.ljp.database.dao

import ai.ljp.database.model.CommentEntity
import androidx.paging.PagingSource
import androidx.room.Dao
import androidx.room.Query
import androidx.room.Upsert
import kotlinx.coroutines.flow.Flow

@Dao
interface CommentDao {
    @Upsert
    suspend fun insertAll(comments : List<CommentEntity>)

    @Query(
        """
            SELECT * FROM comments 
            WHERE `postId` = :postId
        """
    )
    fun getCommentsByPostId (postId : String) : PagingSource<Int, CommentEntity>

    @Query(
        """
            SELECT * FROM comments 
            WHERE `commentId` = :commentId
        """
    )
    fun getCommentById(commentId : String) : Flow<CommentEntity>

    @Query(
        """
            DELETE FROM comments 
            WHERE `commentId` = :commentId
            
        """
    )
    suspend fun deleteComment(commentId : String)
}