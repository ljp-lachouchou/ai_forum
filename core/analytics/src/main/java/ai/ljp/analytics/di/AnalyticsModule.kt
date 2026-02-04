package ai.ljp.analytics.di

import ai.ljp.analytics.AnalyticsHelper
import ai.ljp.analytics.FirebaseAnalyticsHelper
import com.google.firebase.Firebase
import com.google.firebase.analytics.FirebaseAnalytics
import com.google.firebase.analytics.analytics
import dagger.Binds
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
internal abstract class AnalyticsModule {
    @Provides
    @Singleton
    fun provideFirebaseAnalytics(): FirebaseAnalytics = Firebase.analytics

    @Binds
    abstract fun bindsAnalyticsHelper(
        firebaseAnalyticsHelper: FirebaseAnalyticsHelper
    ) : AnalyticsHelper
}