package ai.ljp.database

import ai.ljp.database.dao.BookmarkDao
import ai.ljp.database.dao.CommentDao
import ai.ljp.database.dao.FollowDao
import ai.ljp.database.dao.LikeDao
import ai.ljp.database.dao.NotificationDao
import ai.ljp.database.dao.ProfileDao
import ai.ljp.database.dao.RecentSearchQueryDao
import ai.ljp.database.dao.TreeholeDao
import ai.ljp.database.dao.WordDao
import ai.ljp.database.model.ActiveBookmarkCrossRef
import ai.ljp.database.model.ActiveLikeCrossRef
import ai.ljp.database.model.BookmarkEntity
import ai.ljp.database.model.CommentEntity
import ai.ljp.database.model.FollowEntity
import ai.ljp.database.model.LikeEntity
import ai.ljp.database.model.NotificationEntity
import ai.ljp.database.model.ProfileEntity
import ai.ljp.database.model.RecentSearchQueryEntity
import ai.ljp.database.model.TreeholeEntity
import ai.ljp.database.model.WordEntity
import ai.ljp.database.util.InstantConverter
import ai.ljp.database.util.SyncStateConverter
import ai.ljp.database.util.WordTagConverter
import androidx.room.AutoMigration
import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import androidx.room.migration.AutoMigrationSpec
import androidx.room.migration.Migration
import kotlin.reflect.KClass

@Database(
    entities = [
        BookmarkEntity::class,
        CommentEntity::class,
        FollowEntity::class,
        LikeEntity::class,
        NotificationEntity::class,
        ProfileEntity::class,
        RecentSearchQueryEntity::class,
        TreeholeEntity::class,
        WordEntity::class
    ],
    views = [
        ActiveLikeCrossRef::class,
        ActiveBookmarkCrossRef::class
    ],
    version = 5,
    exportSchema = true,
    autoMigrations = [
        AutoMigration(from = 1, to = 2),
        AutoMigration(from = 2, to = 3),

    ],
)
@TypeConverters(
    InstantConverter::class,
    SyncStateConverter::class,
    WordTagConverter::class
)
internal abstract class AIForumDatabase : RoomDatabase() {
    abstract fun bookmarkDao() : BookmarkDao
    abstract fun commentDao() : CommentDao
    abstract fun followDao() : FollowDao
    abstract fun likeDao() : LikeDao
    abstract fun notificationDao() : NotificationDao
    abstract fun profileDao() : ProfileDao
    abstract fun recentSearchQueryDao() : RecentSearchQueryDao
    abstract fun treeholeDao() : TreeholeDao
    abstract fun wordDao() : WordDao
}
