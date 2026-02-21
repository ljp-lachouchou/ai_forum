package ai.ljp.data.repository

import ai.ljp.data.Syncable
import ai.ljp.database.model.PopulateCommentProfileResource
import androidx.paging.PagingData
import com.ljp.model.Comment
import com.ljp.model.CommentProfileResource
import kotlinx.coroutines.flow.Flow

interface CommentRepository : Syncable {

    suspend fun createComment(
        postId: String,
        content: String
    ) : Boolean

    fun getComments(postId : String) : Flow<PagingData<Comment>>

    fun getCommentById(commentId : String) : Flow<Comment>

    suspend fun deleteComment(commentId : String)

    fun getCommentsProfileResource(postId: String) : Flow<PagingData<CommentProfileResource>>
}