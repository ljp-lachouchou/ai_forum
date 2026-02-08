package ai.ljp.data.repository

import ai.ljp.data.Syncable
import ai.ljp.network.model.LoginResponse
import com.ljp.model.Profile
import kotlinx.coroutines.flow.Flow

interface ProfileRepository : Syncable {

    suspend fun updateProfile(
        userName: String?,
        avatarUrl: String?,
        bio: String?
    )

    suspend fun getSelfProfile() : Flow<Profile>
    suspend fun login(email : String, password : String) : Boolean
    suspend fun register(email : String, password : String): Boolean

}