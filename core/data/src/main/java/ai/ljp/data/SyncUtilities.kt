package ai.ljp.data

import ai.ljp.database.model.help.SyncState
import ai.ljp.datastore.ChangeVersion
import ai.ljp.network.model.ChangelogItem
import ai.ljp.network.model.GetChangeLogResponse
import ai.ljp.network.model.deleted
import android.util.Log
import kotlinx.datetime.Instant
import kotlin.coroutines.cancellation.CancellationException

const val SYNC_LOG_TAG = "Synchronizer.changeSync"
internal const val SYNC_BATCH_SIZE = 40
interface Synchronizer {
    suspend fun getChangeVersion() : ChangeVersion
    suspend fun updateChangeVersion(update : ChangeVersion.() -> ChangeVersion)
    suspend fun performGlobalSync(
        repos : List<Syncable>,
        versionReader :(ChangeVersion) -> Long,
        changeFetcher : suspend (Long) -> GetChangeLogResponse?,
        versionUpdater : ChangeVersion.(Int) -> ChangeVersion,
    ) : Boolean
    suspend fun Syncable.sync(changeList: List<ChangelogItem>) = this@sync.syncWith(
        this@Synchronizer,
        changeList
    )

}
suspend fun <T> suspendRunCatching(block: suspend () -> T): Result<T> = try {
    Result.success(block())
} catch (cancellationException: CancellationException) {
    throw cancellationException
} catch (exception: Exception) {
    Log.e(
        "suspendRunCatching",
        "Failed to evaluate a suspendRunCatchingBlock. Returning failure Result",
        exception,
    )
    Result.failure(exception)
}
interface Syncable {
    suspend fun syncWith(
        synchronizer: Synchronizer,
        changeList: List<ChangelogItem>) : Boolean = suspendRunCatching {
        Log.e(SYNC_LOG_TAG,"table name is $tableName")
        if (changeList.isEmpty()) return@suspendRunCatching true
        Log.e(SYNC_LOG_TAG,"$tableName changeList has element: $changeList")
        val (deleted,updated) = changeList.partition(ChangelogItem::deleted)
        val deletedIds = deleted.map(ChangelogItem::entityId)
        val updatedIds = updated.map(ChangelogItem::entityId)
        Log.e(SYNC_LOG_TAG,"$tableName modelUpdater ids $updatedIds")
        modelUpdater(updatedIds)
        Log.e(SYNC_LOG_TAG,"$tableName modelDeleter ids $deletedIds")
        modelDeleter(deletedIds)
        return@suspendRunCatching true
    }.isSuccess
    val tableName : String
    suspend fun modelDeleter(ids : List<String>)
    suspend fun modelUpdater(changedIds : List<String>)
}

interface UserSync {
    suspend fun updateSync(userId : String,
                           postId : String,
                           syncState : SyncState,
                           updatedAt : Instant)
}

//suspend fun Synchronizer.changeSync(
//    tableName : String,
//    versionReader :(ChangeVersion) -> Long,
//    changeFetcher : suspend (Long) -> GetChangeLogResponse?,
//    versionUpdater : ChangeVersion.(Int) -> ChangeVersion,
//    modelDeleter : suspend (List<String>) -> Unit,
//    modelUpdater : suspend (List<String>) -> Unit
//) = suspendRunCatching {
//    val logTag = "Synchronizer.changeSync"
//    val currentVersion = 0L
//    val nextVersion = currentVersion + 1
//    val resp = changeFetcher(nextVersion) ?: return@suspendRunCatching false
//    Log.e(logTag," changeFetcher start")
//    Log.e(logTag,"${resp.changes}")
//    Log.e(logTag,"table name is $tableName")
//    val changeList = resp.changes
//        .filter {
//            it.entityType == tableName
//        }
//
//    val lastVersion = resp.latestVersion
//    if (changeList.isEmpty()) return@suspendRunCatching true
//    Log.e(logTag,"$tableName changeList has element: $changeList")
//    val (deleted,updated) = changeList.partition(ChangelogItem::deleted)
//    val deletedIds = deleted.map(ChangelogItem::entityId).distinct()
//    modelDeleter(deletedIds)
//    Log.e(logTag,"$tableName modelDeleter ids $deletedIds")
//    val updatedIds = updated.map(ChangelogItem::entityId).distinct()
//    modelUpdater(updatedIds)
//    Log.e(logTag,"$tableName modelUpdater ids $updatedIds")
//    updateChangeVersion {
//        versionUpdater(lastVersion)
//    }
//}.isSuccess