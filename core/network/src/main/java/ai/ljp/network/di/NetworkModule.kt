package ai.ljp.network.di

import ai.ljp.network.AIForumNetworkDataSource
import ai.ljp.network.ktor.KtorAIForumNetwork
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
internal interface NetworkModule {
    @Binds
    fun bindsNetworkDataSource(
        ktorNetworkDataSource: KtorAIForumNetwork
    ) : AIForumNetworkDataSource



}