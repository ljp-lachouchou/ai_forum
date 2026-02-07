package ai.ljp.data.repository.impl

import ai.ljp.data.SYNC_BATCH_SIZE
import ai.ljp.data.Synchronizer
import ai.ljp.data.changeSync
import ai.ljp.data.model.asDBModel
import ai.ljp.data.repository.ProfileRepository
import ai.ljp.database.dao.ProfileDao
import ai.ljp.database.model.ProfileEntity
import ai.ljp.database.model.asExtraModel
import ai.ljp.database.model.roomTableName
import ai.ljp.datastore.AIForumPreferencesDatastore
import ai.ljp.datastore.ChangeVersion
import ai.ljp.network.AIForumNetworkDataSource
import ai.ljp.network.ktor.KtorAIForumNetwork
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
            profileId = userData.currentUserId,
            userName = userName,
            avatarUrl = avatarUrl,
            bio = bio
        )
    }

    override suspend fun getSelfProfile(): Flow<Profile> {
        val userData = preferencesDatastore.userData.first()
        return profileDao.getSelfProfile(
            userId = userData.currentUserId
        ).map(ProfileEntity::asExtraModel)
    }
    override suspend fun syncWith(synchronizer: Synchronizer): Boolean =
        synchronizer.changeSync(
            networkEntity = ProfileEntity::class,
            versionReader = ChangeVersion::syncVersion,
            changeFetcher = {sinceVersion ->
                network.getChangelogs(since = sinceVersion)
            },
            versionUpdater = {lastVersion ->
                ChangeVersion(syncVersion = 1L * lastVersion)
            },
            entityTagger = {entityClass ->
                entityClass.roomTableName
            },
            modelDeleter = profileDao::deleteAll,
            modelUpdater = { changedIds ->//分批
                changedIds.chunked(SYNC_BATCH_SIZE).forEach { ids ->
                    val items = network.syncProfiles(profileIds = ids)
                        ?.map(SyncProfileItem::asDBModel) ?: return@forEach
                    profileDao.upsertProfiles(items)
                }
            }
        )
}