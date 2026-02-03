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
    @Query(
        """
            SELECT * 
            FROM profiles 
            WHERE `profileId` = :userId
        """
    )
    fun getSelfProfile(userId : String) : Flow<ProfileEntity>
}