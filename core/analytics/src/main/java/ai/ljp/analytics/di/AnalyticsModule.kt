package ai.ljp.analytics.di

import ai.ljp.analytics.AnalyticsHelper
import ai.ljp.analytics.FirebaseAnalyticsHelper
import android.content.Context
import com.google.firebase.Firebase
import com.google.firebase.FirebaseApp
import com.google.firebase.analytics.FirebaseAnalytics
import com.google.firebase.analytics.analytics
import dagger.Binds
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
internal abstract class AnalyticsModule {
    companion object {
        @Provides
        @Singleton
        fun provideFirebaseAnalytics(
            @ApplicationContext context : Context
        ): FirebaseAnalytics {
            if (FirebaseApp.getApps(context).isEmpty()) {
                FirebaseApp.initializeApp(context)
            }
            return Firebase.analytics
        }
    }

    @Binds
    abstract fun bindsAnalyticsHelper(
        firebaseAnalyticsHelper: FirebaseAnalyticsHelper
    ) : AnalyticsHelper
}