package ai.ljp.data.repository

import ai.ljp.data.Syncable
import ai.ljp.data.model.Like
import kotlinx.coroutines.flow.Flow

interface LikeRepository : Syncable {
    suspend fun insertAll(likes : List<Like>)

    suspend fun delete(userId : String,postId : String)

    suspend fun toggleLike(
        userId : String,
        postId: String
    )
    fun getLikesPostId(profileId : String) : List<String>

    fun markLike(postId : String,userId: String) : Flow<Boolean>

}