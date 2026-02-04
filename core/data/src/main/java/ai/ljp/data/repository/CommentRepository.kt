package ai.ljp.data.repository

import ai.ljp.data.Syncable
import androidx.paging.PagingData
import com.ljp.model.Comment
import kotlinx.coroutines.flow.Flow

interface CommentRepository : Syncable {

    suspend fun insertAll(comments : List<Comment>)

    fun getComments(postId : String) : Flow<PagingData<Comment>>

    fun getCommentById(commentId : String) : Flow<Comment>

    suspend fun deleteComment(commentId : String)
}