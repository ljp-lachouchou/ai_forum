package ai.ljp.sync.status

import ai.ljp.sync.initializer.SYNC_WORK_NAME
import ai.ljp.sync.worker.SyncWorker
import android.content.Context
import androidx.work.ExistingWorkPolicy
import androidx.work.WorkInfo
import androidx.work.WorkManager
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.conflate
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class WorkManagerSyncManager @Inject constructor(
    @ApplicationContext private val context: Context
) : SyncManager{
    override val isSyncing: Flow<Boolean> =
        WorkManager.getInstance(context)
            .getWorkInfosForUniqueWorkFlow(SYNC_WORK_NAME) //work manager使用enqueueUniqueWork
            .map(List<WorkInfo>::anyRunning)
            .conflate() //总收集最新值



    override fun requestSync() {
        val workManager = WorkManager.getInstance(context)
        workManager.enqueueUniqueWork(
            uniqueWorkName = SYNC_WORK_NAME,
            existingWorkPolicy = ExistingWorkPolicy.KEEP,
            request = SyncWorker.startSyncWork()
            )//同步任务
    }
}
private fun List<WorkInfo>.anyRunning() = any { it.state == WorkInfo.State.RUNNING }