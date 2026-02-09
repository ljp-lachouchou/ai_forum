package ai.ljp.data.repository

import ai.ljp.data.Syncable
import ai.ljp.data.model.Like
import kotlinx.coroutines.flow.Flow
import kotlinx.datetime.Instant

interface LikeRepository : Syncable {
    suspend fun insertAll(likes : List<Like>)

    suspend fun delete(userId : String,postId : String)

    suspend fun toggleLike(
        userId : String,
        postId: String,
        updatedAt : Instant
    )

    fun markLike(postId : String,userId: String) : Flow<Boolean>

}