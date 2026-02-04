package ai.ljp.sync.status

import kotlinx.coroutines.flow.Flow

interface SyncManager {
    val isSyncing : Flow<Boolean>
    fun requestSync()
}