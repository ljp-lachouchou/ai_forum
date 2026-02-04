package ai.ljp.sync.di

import ai.ljp.sync.status.FirebaseSubscriber
import ai.ljp.sync.status.SyncManager
import ai.ljp.sync.status.SyncSubscriber
import ai.ljp.sync.status.WorkManagerSyncManager
import com.google.firebase.Firebase
import com.google.firebase.messaging.FirebaseMessaging
import com.google.firebase.messaging.messaging
import dagger.Binds
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class SyncModule {

    @Binds
    abstract fun bindsSyncSubscriber(
        subscriber: FirebaseSubscriber
    ) : SyncSubscriber
    @Binds
    abstract fun bindsSyncManager(
        syncManager : WorkManagerSyncManager
    ) : SyncManager
    companion object {
        @Provides
        @Singleton
        fun providersFirebaseMessaging() : FirebaseMessaging = Firebase.messaging
    }
}