package ai.ljp.data.repository

import ai.ljp.data.Syncable
import ai.ljp.data.UserSync
import ai.ljp.data.model.Follow
import kotlinx.datetime.Instant

interface FollowRepository : Syncable, UserSync{
    suspend fun insertAll(follows : List<Follow>)

    suspend fun delete(userId : String,followId : String)

    suspend fun toggleFollow(
        userId: String,
        followId : String,
        deleted : Boolean,
        updatedAt : Instant
    )
}