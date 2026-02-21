package ai.ljp.database.di

import ai.ljp.database.AIForumDatabase
import ai.ljp.database.dao.BookmarkDao
import ai.ljp.database.dao.CommentDao
import ai.ljp.database.dao.FollowDao
import ai.ljp.database.dao.LikeDao
import ai.ljp.database.dao.NotificationDao
import ai.ljp.database.dao.ProfileDao
import ai.ljp.database.dao.RecentSearchQueryDao
import ai.ljp.database.dao.TreeholeDao
import ai.ljp.database.dao.WordDao
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
internal object DaoModule {
    @Provides
    @Singleton
    fun providersBookmarkDao(
        aiForumDatabase: AIForumDatabase
    ) : BookmarkDao = aiForumDatabase.bookmarkDao()

    @Provides
    @Singleton
    fun providersCommentDao(
        aiForumDatabase: AIForumDatabase
    ) : CommentDao = aiForumDatabase.commentDao()
    @Provides
    @Singleton
    fun providersFollowDao(
        aiForumDatabase: AIForumDatabase
    ) : FollowDao = aiForumDatabase.followDao()
    @Provides
    @Singleton
    fun providersLikeDao(
        aiForumDatabase: AIForumDatabase
    ) : LikeDao = aiForumDatabase.likeDao()
    @Provides
    @Singleton
    fun providersNotificationDao(
        aiForumDatabase: AIForumDatabase
    ) : NotificationDao = aiForumDatabase.notificationDao()
    @Provides
    @Singleton
    fun providersProfileDao(
        aiForumDatabase: AIForumDatabase
    ) : ProfileDao = aiForumDatabase.profileDao()
    @Provides
    @Singleton
    fun providersRecentSearchQueryDao(
        aiForumDatabase: AIForumDatabase
    ) : RecentSearchQueryDao = aiForumDatabase.recentSearchQueryDao()
    @Provides
    @Singleton
    fun providersTreeholeDao(
        aiForumDatabase: AIForumDatabase
    ) : TreeholeDao = aiForumDatabase.treeholeDao()
    @Provides
    @Singleton
    fun providersWordDao(
        aiForumDatabase: AIForumDatabase
    ) : WordDao = aiForumDatabase.wordDao()
}