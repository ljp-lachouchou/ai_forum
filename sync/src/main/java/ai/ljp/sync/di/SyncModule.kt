package ai.ljp.sync.di

import ai.ljp.sync.status.SyncManager
import ai.ljp.sync.status.WorkManagerSyncManager
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
abstract class SyncModule {

//    @Binds
//    abstract fun bindsSyncSubscriber(
//        subscriber: FirebaseSubscriber
//    ) : SyncSubscriber
    @Binds
    abstract fun bindsSyncManager(
        syncManager : WorkManagerSyncManager
    ) : SyncManager
    companion object {
//        @Provides
//        @Singleton
//        fun providersFirebaseMessaging() : FirebaseMessaging = Firebase.messaging
    }
}