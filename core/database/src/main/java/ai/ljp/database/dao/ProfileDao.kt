package ai.ljp.database.dao

import ai.ljp.database.model.ProfileEntity
import androidx.room.Dao
import androidx.room.Query
import androidx.room.Upsert
import kotlinx.coroutines.flow.Flow

@Dao
interface ProfileDao {
    @Upsert
    suspend fun upsertProfile(profile : ProfileEntity)

    @Upsert
    suspend fun upsertProfiles(profiles : List<ProfileEntity>)
    @Query(
        """
            SELECT * 
            FROM profiles 
            WHERE `profileId` = :userId
        """
    )
    fun getProfile(userId : String) : Flow<ProfileEntity>

    @Query(
        value = """
            DELETE FROM profiles
            WHERE `profileId` in (:ids)
        """,
    )
    suspend fun deleteAll(ids : List<String>)
}