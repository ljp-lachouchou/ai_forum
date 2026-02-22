package ai.ljp.data.di

import ai.ljp.data.repository.BookmarkRepository
import ai.ljp.data.repository.CommentRepository
import ai.ljp.data.repository.FollowRepository
import ai.ljp.data.repository.InteractionWordRepository
import ai.ljp.data.repository.LikeRepository
import ai.ljp.data.repository.NotificationRepository
import ai.ljp.data.repository.ProfileRepository
import ai.ljp.data.repository.RecentSearchRepository
import ai.ljp.data.repository.TreeholeRepository
import ai.ljp.data.repository.UserDataRepository
import ai.ljp.data.repository.WordRepository
import ai.ljp.data.repository.impl.DefaultRecentSearchRepository
import ai.ljp.data.repository.impl.OfflineFirstBookmarkRepository
import ai.ljp.data.repository.impl.OfflineFirstCommentRepository
import ai.ljp.data.repository.impl.OfflineFirstFollowRepository
import ai.ljp.data.repository.impl.OfflineFirstInteractionWordRepository
import ai.ljp.data.repository.impl.OfflineFirstNotificationRepository
import ai.ljp.data.repository.impl.OfflineFirstProfileRepository
import ai.ljp.data.repository.impl.OfflineFirstTreeholeRepository
import ai.ljp.data.repository.impl.OfflineFirstUserDataRepository
import ai.ljp.data.repository.impl.OfflineFirstWordRepository
import ai.ljp.data.repository.impl.OfflineFistLikeRepository
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
interface RepositoryModule {
    @Binds
    @Singleton
    fun bindsBookmarkRepo(
        bookmarkRepo : OfflineFirstBookmarkRepository
    ) : BookmarkRepository
    @Binds
    @Singleton
    fun bindsCommentRepository(
        commentRepo : OfflineFirstCommentRepository
    ) : CommentRepository

    @Binds
    @Singleton
    fun bindsFollowRepository(
        followRepo : OfflineFirstFollowRepository
    ) : FollowRepository

    @Binds
    @Singleton
    fun bindsInteractionRepo(
        interactionWordRepository: OfflineFirstInteractionWordRepository
    ) : InteractionWordRepository

    @Binds
    @Singleton
    fun bindsLikeRepo(
        likeRepo : OfflineFistLikeRepository
    ) : LikeRepository

    @Binds
    @Singleton
    fun bindsNotificationRepo(
        notificationRepository: OfflineFirstNotificationRepository
    ) : NotificationRepository

    @Binds
    @Singleton
    fun bindsProfileRepo(
        profileRepo : OfflineFirstProfileRepository
    ) : ProfileRepository

    @Binds
    @Singleton
    fun bindsRSRRepo(
        defaultRecentSearchRepository: DefaultRecentSearchRepository
    ) : RecentSearchRepository

    @Binds
    @Singleton
    fun bindsTreeholeRepo(
        treeholeRepository: OfflineFirstTreeholeRepository
    ) : TreeholeRepository

    @Binds
    @Singleton
    fun bindUserDataRepo(
        userdataRepo : OfflineFirstUserDataRepository
    ) : UserDataRepository

    @Binds
    @Singleton
    fun bindsWordRepo(
        wordRepo : OfflineFirstWordRepository
    ) : WordRepository
}