package ai.ljp.data

import ai.ljp.database.model.BaseEntity
import ai.ljp.database.model.help.SyncState
import ai.ljp.database.model.roomTableName
import ai.ljp.datastore.ChangeVersion
import ai.ljp.network.model.ChangelogItem
import ai.ljp.network.model.GetChangeLogResponse
import ai.ljp.network.model.deleted
import android.util.Log
import kotlinx.datetime.Instant
import kotlin.coroutines.cancellation.CancellationException
import kotlin.reflect.KClass
internal const val SYNC_BATCH_SIZE = 40
interface Synchronizer {
    suspend fun getChangeVersion() : ChangeVersion
    suspend fun updateChangeVersion(update : ChangeVersion.() -> ChangeVersion)

    suspend fun Syncable.sync() = this@sync.syncWith(this@Synchronizer)

}
private suspend fun <T> suspendRunCatching(block: suspend () -> T): Result<T> = try {
    Result.success(block())
} catch (cancellationException: CancellationException) {
    throw cancellationException
} catch (exception: Exception) {
    Log.i(
        "suspendRunCatching",
        "Failed to evaluate a suspendRunCatchingBlock. Returning failure Result",
        exception,
    )
    Result.failure(exception)
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

suspend fun <T : BaseEntity> Synchronizer.changeSync(
    networkEntity : KClass<T>,
    versionReader :(ChangeVersion) -> Long,
    changeFetcher : suspend (Long) -> GetChangeLogResponse?,
    versionUpdater : ChangeVersion.(Int) -> ChangeVersion,
    entityTagger : (Class<T>) -> String,
    modelDeleter : suspend (List<String>) -> Unit,
    modelUpdater : suspend (List<String>) -> Unit
) = suspendRunCatching {
    val currentVersion = versionReader(getChangeVersion())
    val nextVersion = currentVersion + 1
    val resp = changeFetcher(nextVersion) ?: return@suspendRunCatching true
    val tableName = entityTagger(networkEntity.java)
    val changeList = resp.changes
        .filter {
            it.type == tableName
        }
    val lastVersion = resp.latestVersion
    if (changeList.isEmpty()) return@suspendRunCatching true
    val (deleted,updated) = changeList.partition(ChangelogItem::deleted)
    modelDeleter(deleted.map(ChangelogItem::id))
    modelUpdater(updated.map(ChangelogItem::id))
    updateChangeVersion {
        versionUpdater(lastVersion)
    }
}.isSuccess