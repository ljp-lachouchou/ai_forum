package ai.ljp.data.di

import ai.ljp.data.util.BroadcastTimezoneMonitor
import ai.ljp.data.util.ConnectivityManagerNetworkMonitor
import ai.ljp.data.util.NetworkMonitor
import ai.ljp.data.util.TimezoneMonitor
import dagger.Binds
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class UtilModule {
    @Binds
    @Singleton
    abstract fun providersNetworkMonitor(
        networkMonitor: ConnectivityManagerNetworkMonitor
    ) : NetworkMonitor

    @Binds
    @Singleton
    abstract fun providersTimezoneMonitor(
        timezoneMonitor: BroadcastTimezoneMonitor,
    ) : TimezoneMonitor
}