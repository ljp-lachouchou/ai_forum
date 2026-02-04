package ai.ljp.data

import ai.ljp.database.model.help.SyncState
import ai.ljp.datastore.ChangeVersion
import kotlinx.datetime.Instant

interface Synchronizer {
    suspend fun getChangeVersion() : ChangeVersion
    suspend fun updateChangeVersion(update : ChangeVersion.() -> ChangeVersion)

    suspend fun Syncable.sync() = this@sync.syncWith(this@Synchronizer)

}

interface Syncable {
    suspend fun syncWith(synchronizer: Synchronizer) : Boolean
}

interface UserSync {
    suspend fun updateSync(userId : String,
                           postId : String,
                           syncState : SyncState,
                           updatedAt : Instant)
}