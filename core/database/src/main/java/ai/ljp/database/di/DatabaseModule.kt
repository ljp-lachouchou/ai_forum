package ai.ljp.database.di

import ai.ljp.database.AIForumDatabase
import ai.ljp.database.DatabaseMigrations
import android.content.Context
import androidx.room.Room
import androidx.room.RoomDatabase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
internal object DatabaseModule {
    @Provides
    @Singleton
    fun providersDatabase(
        @ApplicationContext context : Context
    ) : AIForumDatabase =
        Room.databaseBuilder(
            context = context,
            klass = AIForumDatabase::class.java,
            name = "aiforum-database"
        )
            .addMigrations(
                DatabaseMigrations.MIGRATION_3_4,
                DatabaseMigrations.MIGRATION_4_5
            )
            .build()
}
