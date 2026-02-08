package ai.ljp.data.repository.impl

import ai.ljp.data.SYNC_BATCH_SIZE
import ai.ljp.data.model.asDBModel
import ai.ljp.data.repository.ProfileRepository
import ai.ljp.database.dao.ProfileDao
import ai.ljp.database.model.ProfileEntity
import ai.ljp.database.model.asExtraModel
import ai.ljp.datastore.AIForumPreferencesDatastore
import ai.ljp.network.AIForumNetworkDataSource
import ai.ljp.network.model.SyncProfileItem
import com.ljp.model.Profile
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map
import javax.inject.Inject
import kotlin.collections.chunked

class OfflineFirstProfileRepository @Inject constructor(
    private val network: AIForumNetworkDataSource,
    private val preferencesDatastore: AIForumPreferencesDatastore,
    private val profileDao: ProfileDao
) : ProfileRepository{
    override suspend fun updateProfile(userName: String?, avatarUrl: String?, bio: String?) {
        val userData = preferencesDatastore.userData.first()
        network.updatedProfile(
            profileId = userData.currentUserId!!,
            userName = userName,
            avatarUrl = avatarUrl,
            bio = bio
        )
    }

    override suspend fun getSelfProfile(): Flow<Profile> {
        val userData = preferencesDatastore.userData.first()
        return profileDao.getSelfProfile(
            userId = userData.currentUserId!!
        ).map(ProfileEntity::asExtraModel)
    }

    override suspend fun login(email: String, password: String) : Boolean {
        val resp = network.login(email = email, password = password) ?: return false
        preferencesDatastore.setAuthToken(resp.accessToken)
        preferencesDatastore.setCurrentUserId(resp.userId)
        return true
    }

    override suspend fun register(email: String, password: String) : Boolean {
        network.register(email = email, password = password) ?: return false
        return true
    }

    override val tableName: String
        get() = "profiles"

    override suspend fun modelDeleter(ids: List<String>)=
        profileDao.deleteAll(ids)

    override suspend fun modelUpdater(changedIds: List<String>) {
        changedIds.chunked(SYNC_BATCH_SIZE).forEach { ids ->
            val items = network.syncProfiles(profileIds = ids)
                ?.map(SyncProfileItem::asDBModel) ?: return@forEach
            profileDao.upsertProfiles(items)
        }
    }
}