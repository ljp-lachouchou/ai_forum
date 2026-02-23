package ai.ljp.sync.worker

import android.content.Context
import android.util.Log
import androidx.hilt.work.HiltWorkerFactory
import androidx.work.CoroutineWorker
import androidx.work.Data
import androidx.work.ForegroundInfo
import androidx.work.WorkerParameters
import dagger.hilt.EntryPoint
import dagger.hilt.InstallIn
import dagger.hilt.android.EntryPointAccessors
import dagger.hilt.components.SingletonComponent
import kotlin.reflect.KClass

@EntryPoint
@InstallIn(SingletonComponent::class)
interface HiltWorkerFactoryEntryPoint {
    fun hiltWorkerFactory() : HiltWorkerFactory
}
private const val WORKER_CLASS_NAME = "RouterWorkerDelegateClassName"

internal fun KClass<out  CoroutineWorker>.delegatedData() =
    Data.Builder()
        .putString(WORKER_CLASS_NAME,qualifiedName)
        .build()
class DelegatorWorker(
    appContext : Context,
    params : WorkerParameters
) : CoroutineWorker(appContext,params) {
    private val workerClassName =
            params.inputData.getString(WORKER_CLASS_NAME) ?: ""

    private val delegateWorker =
        EntryPointAccessors.fromApplication<HiltWorkerFactoryEntryPoint>(appContext)
            .hiltWorkerFactory()
            .createWorker(appContext,workerClassName,params)
            as? CoroutineWorker
            ?: throw IllegalArgumentException("Unable to find appropriate worker")

    override suspend fun getForegroundInfo(): ForegroundInfo = delegateWorker.getForegroundInfo()

    override suspend fun doWork(): Result {
        Log.e("Delegator", "Delegator received task, target: ${inputData.getString("WORKER_CLASS_NAME")}")
        return delegateWorker.doWork()
    }


}