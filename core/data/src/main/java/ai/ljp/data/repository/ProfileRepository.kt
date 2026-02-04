package ai.ljp.data.repository

import ai.ljp.data.Syncable
import com.ljp.model.Profile
import kotlinx.coroutines.flow.Flow

interface ProfileRepository : Syncable {

    suspend fun upsertProfile(profile : Profile)

    suspend fun upsertProfiles(profiles : List<Profile>)
    fun getSelfProfile(userId : String) : Flow<Profile>

}