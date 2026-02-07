package ai.ljp.data.repository

import ai.ljp.data.Syncable
import com.ljp.model.Profile
import kotlinx.coroutines.flow.Flow

interface ProfileRepository : Syncable {

    suspend fun updateProfile(
        userName: String?,
        avatarUrl: String?,
        bio: String?
    )

    suspend fun getSelfProfile() : Flow<Profile>

}