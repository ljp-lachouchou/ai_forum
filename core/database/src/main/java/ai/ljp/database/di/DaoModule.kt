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

@Module
@InstallIn(SingletonComponent::class)
internal object DaoModule {
    @Provides
    fun providersBookmarkDao(
        aiForumDatabase: AIForumDatabase
    ) : BookmarkDao = aiForumDatabase.bookmarkDao()

    @Provides
    fun providersCommentDao(
        aiForumDatabase: AIForumDatabase
    ) : CommentDao = aiForumDatabase.commentDao()
    @Provides
    fun providersFollowDao(
        aiForumDatabase: AIForumDatabase
    ) : FollowDao = aiForumDatabase.followDao()
    @Provides
    fun providersLikeDao(
        aiForumDatabase: AIForumDatabase
    ) : LikeDao = aiForumDatabase.likeDao()
    @Provides
    fun providersNotificationDao(
        aiForumDatabase: AIForumDatabase
    ) : NotificationDao = aiForumDatabase.notificationDao()
    @Provides
    fun providersProfileDao(
        aiForumDatabase: AIForumDatabase
    ) : ProfileDao = aiForumDatabase.profileDao()
    @Provides
    fun providersRecentSearchQueryDao(
        aiForumDatabase: AIForumDatabase
    ) : RecentSearchQueryDao = aiForumDatabase.recentSearchQueryDao()
    @Provides
    fun providersTreeholeDao(
        aiForumDatabase: AIForumDatabase
    ) : TreeholeDao = aiForumDatabase.treeholeDao()
    @Provides
    fun providersWordDao(
        aiForumDatabase: AIForumDatabase
    ) : WordDao = aiForumDatabase.wordDao()
}