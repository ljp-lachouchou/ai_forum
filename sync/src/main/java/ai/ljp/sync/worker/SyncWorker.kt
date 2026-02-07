package ai.ljp.sync.worker

import ai.ljp.analytics.AnalyticsHelper
import ai.ljp.data.Synchronizer
import ai.ljp.data.repository.BookmarkRepository
import ai.ljp.data.repository.CommentRepository
import ai.ljp.data.repository.FollowRepository
import ai.ljp.data.repository.LikeRepository
import ai.ljp.data.repository.NotificationRepository
import ai.ljp.data.repository.ProfileRepository
import ai.ljp.data.repository.TreeholeRepository
import ai.ljp.data.repository.WordRepository
import ai.ljp.datastore.AIForumPreferencesDatastore
import ai.ljp.datastore.ChangeVersion
import ai.ljp.sync.initializer.SyncConstraints
import ai.ljp.sync.initializer.syncForegroundInfo
import ai.ljp.sync.status.SyncSubscriber
import android.content.Context
import androidx.hilt.work.HiltWorker
import androidx.work.CoroutineWorker
import androidx.work.ForegroundInfo
import androidx.work.OneTimeWorkRequestBuilder
import androidx.work.OutOfQuotaPolicy
import androidx.work.WorkerParameters
import com.ljp.common.network.AIForumDispatchers
import com.ljp.common.network.Dispatcher
import dagger.assisted.Assisted
import dagger.assisted.AssistedInject
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.async
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.withContext

@HiltWorker
class SyncWorker @AssistedInject constructor(
    @Assisted private val appContext : Context,
    @Assisted params : WorkerParameters,
    private val aiForumPreferences: AIForumPreferencesDatastore,
    private val bookmarkRepository: BookmarkRepository,
    private val likeRepository: LikeRepository,
    private val commentRepository: CommentRepository,
    private val followRepository: FollowRepository,
    private val notificationRepository: NotificationRepository,
    private val profileRepository: ProfileRepository,
    private val treeholeRepository: TreeholeRepository,
    private val wordRepository: WordRepository,
    @Dispatcher(AIForumDispatchers.IO) private val ioDispatcher: CoroutineDispatcher,
    private val analyticsHelper: AnalyticsHelper,
) : CoroutineWorker(appContext,params), Synchronizer {
    /*
    当 WorkManager 需要把任务提升为 前台任务（尤其是 expedited 或长任务）时：

    Android 要求显示一个通知

    ForegroundInfo 就是通知配置（标题、渠道、图标等）
     */
    override suspend fun getForegroundInfo(): ForegroundInfo =
        appContext.syncForegroundInfo()
    override suspend fun doWork(): Result = withContext(ioDispatcher) {
        analyticsHelper.logSyncStarted()
        val syncSuccessfully = true
            awaitAll(
            async { bookmarkRepository.sync() },
            async { likeRepository.sync() },
            async { commentRepository.sync() },
            async { followRepository.sync() },
            async { notificationRepository.sync() },
            async { profileRepository.sync() },
            async { treeholeRepository.sync() },
            async { wordRepository.sync() }
        ).all { it }
        analyticsHelper.logSyncFinished(syncSuccessfully)
        if (syncSuccessfully) {
            Result.success()
        }else {
            Result.retry()
        }
    }
    override suspend fun getChangeVersion(): ChangeVersion =
        aiForumPreferences.getChangeVersion()

    override suspend fun updateChangeVersion(update: ChangeVersion.() -> ChangeVersion)  =
        aiForumPreferences.updateChangeVersion(update)

    companion object {
        fun startSyncWork() = OneTimeWorkRequestBuilder<DelegatorWorker>()
            //降级策略:以不加急运行
            .setExpedited(OutOfQuotaPolicy.RUN_AS_NON_EXPEDITED_WORK_REQUEST) //加急任务
            .setConstraints(SyncConstraints)
            .setInputData(SyncWorker::class.delegatedData())
            .build()
    }

}