package ai.ljp.network.di

import ai.ljp.network.BuildConfig
import ai.ljp.network.supabase.Actor
import ai.ljp.network.supabase.SupabaseActor
import dagger.Binds
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import io.github.jan.supabase.SupabaseClient
import io.github.jan.supabase.createSupabaseClient
import io.github.jan.supabase.storage.Storage
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class SupabaseModule {
    companion object {
        @Provides
        @Singleton
        fun providersSupabaseClient() : SupabaseClient  = createSupabaseClient(
            supabaseUrl = BuildConfig.SUPABASE_URL,
            supabaseKey = BuildConfig.SUPABASE_KEY
        ) {
            install(Storage)
        }
    }
    @Binds
    @Singleton
    abstract fun bindsSupabaseActor(
        supabaseActor: SupabaseActor
    ) : Actor

}