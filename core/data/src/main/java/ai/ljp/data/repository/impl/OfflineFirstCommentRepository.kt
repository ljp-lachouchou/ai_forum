package ai.ljp.data.repository.impl

import ai.ljp.data.FeedPagingConfig
import ai.ljp.data.SYNC_BATCH_SIZE
import ai.ljp.data.model.asDBModel
import ai.ljp.data.repository.CommentRepository
import ai.ljp.database.dao.CommentDao
import ai.ljp.database.model.CommentEntity
import ai.ljp.database.model.PopulateCommentProfileResource
import ai.ljp.database.model.asExtraModel
import ai.ljp.datastore.AIForumPreferencesDatastore
import ai.ljp.network.AIForumNetworkDataSource
import ai.ljp.network.model.SyncCommentItem
import androidx.paging.Pager
import androidx.paging.PagingData
import androidx.paging.map
import com.ljp.model.Comment
import com.ljp.model.CommentProfileResource
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map
import javax.inject.Inject
import kotlin.collections.chunked

class OfflineFirstCommentRepository @Inject constructor(
    private val network : AIForumNetworkDataSource,
    private val commentDao: CommentDao,
    private val preferencesDatastore: AIForumPreferencesDatastore
) : CommentRepository{
    override suspend fun createComment(postId: String, content: String) {
        val userData = preferencesDatastore.userData.first()
        network.createComment(
            postId = postId,
            content = content,
            authorId = userData.currentUserId!!
        )
    }

    override fun getComments(postId: String): Flow<PagingData<Comment>> =
        Pager(
            config = FeedPagingConfig,
            pagingSourceFactory = {
                commentDao.getCommentsByPostId(postId)
            }
        )
            .flow
            .map { pagingData->
                pagingData.map(CommentEntity::asExtraModel)
            }

    override fun getCommentById(commentId: String): Flow<Comment> =
        commentDao.getCommentById(commentId)
            .map(CommentEntity::asExtraModel)

    override suspend fun deleteComment(commentId: String) {
        val userData = preferencesDatastore.userData.first()
        network.deleteComment(
            commentId = commentId,
            authorId = userData.currentUserId!!
            )
    }

    override fun getCommentsProfileResource(postId: String): Flow<PagingData<CommentProfileResource>> =
        Pager(
            config = FeedPagingConfig,
            pagingSourceFactory = {
                commentDao.getCommentsProfileResource(postId)
            }
        ).flow
            .map{pagingData ->
                pagingData.map(PopulateCommentProfileResource::asExtraModel)
            }



    override val tableName: String
        get() = "comments"

    override suspend fun modelDeleter(ids: List<String>) =
        commentDao.deleteAll(ids)

    override suspend fun modelUpdater(changedIds: List<String>) {
        changedIds.chunked(SYNC_BATCH_SIZE).forEach { ids ->
            val items = network.syncComments(ids = ids)
                ?.map(SyncCommentItem::asDBModel) ?: return@forEach
            commentDao.insertAll(items)
        }
    }
}