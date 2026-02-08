package ai.ljp.network

import ai.ljp.network.model.*
import com.ljp.model.WordTag

//定义网络接口
interface AIForumNetworkDataSource {
    suspend fun login(email : String, password : String) : LoginResponse?

    suspend fun register(email: String,password: String) : RegisterResponse?

    suspend fun getProfile(profileId : String) : ProfileResponse?

    suspend fun updatedProfile(
        profileId : String,
        userName : String?,
        avatarUrl : String?,bio : String?
    ) : ProfileResponse?

    suspend fun updateStats(
        userId : String,category : String,
        tag : List<String>,duration : Int
    ) : PersonaUpdateStatsResponse?

    suspend fun updateAvailable(
        userId : String
    ) : PersonaUpdateAvailableResponse?

    suspend fun createWord(
        authorId : String,
        wordUrl : String,
        category : String,
        tags : List<WordTag>,
        wordName : String
    ) : WordDetailResponse?

    suspend fun updateWord(
        wordId : String,
        authorId : String,
        wordUpdateRequesst: WordUpdateRequesst
    )

    suspend fun submitWord(
        wordId : String,
        authorId : String
    ) : Boolean

    suspend fun publishWord(
        wordId : String,
        adminId : String
    ) : Boolean

    suspend fun rejectWord(
        wordId : String,
        adminId : String,
        reson : String
    )

    suspend fun archiveWord(
        wordId : String,
        adminId : String
    )

    suspend fun deleteWord(
        wordId : String
    )

    suspend fun getWord(
        wordId : String
    ) : WordDetailResponse?

    suspend fun getWords(
        mode : String,
        category : String?,
        userId : String?,
        limit : Int?
    ) : List<WordFeedItem>?

    suspend fun aiSearch(
        userId : String,
        query : String
    ) : AISearchResponse?

    suspend fun aiReview(
        postId : String
    )
    suspend fun aiAssistPost(
        content : String
    ) : AIAssistPostResponse?

    suspend fun createComment(
        postId : String,
        authorId : String,
        content : String
    ) : CommentCreateResponse?

    suspend fun getComments(
        postId : String
    ) : List<CommentItem>?

    suspend fun deleteComment(
        commentId : String,
        authorId : String
    )

    suspend fun toggleLike(
        postId : String,
        userId : String
    ) : Boolean

    suspend fun toggleBookmark(
        postId : String,
        userId : String
    ) : Boolean

    suspend fun getLikes(
        userId : String,
    ) : List<UserLikeItem>?

    suspend fun createTreehole(
        authorId : String,
        content : String,
        isAnonymous : Boolean
    ) : TreeholeCreateResponse?

    suspend fun getTreeholes(
        limit : Int?,
        offset : Int?,
        includeAI : Boolean? = true
    ) : List<TreeholeItem>?

    suspend fun treeholeAIReply(
        treeholeId : String,
        content: String
    ) : TreeholeAiReplyResponse?

    suspend fun createNotification(
        userId : String,
        type: String,
        content : String,
        refType : String?,
        refId : String?
    ) : NotificationCreateResponse?

    suspend fun getNotifications(
        userId : String,
        unreadOnly : Boolean?,
        limit : Int?
    ) : List<NotificationItem>?

    suspend fun readNotification(
        userId : String,
        notificationIds : List<String>
    ) : NotificationReadResponse?

    suspend fun readAllNotification(
        userId : String
    ) : NotificationReadAllResponse?

    suspend fun createFollow(
        userId : String,
        followId : String
    ) : FollowCreateResponse?

    suspend fun deleteFollow(
        userId : String,
        followId : String
    )

    suspend fun getFollows(
        userId: String
    ) : List<FollowItem>?

    suspend fun createReport(
        reporterId : String?,
        targetType : String,
        targetId : String,
        reson : String
    ) : ReportCreateResponse?

    suspend fun getChangelogs(
        since : Long,
        limit : Int? = null
    ) : GetChangeLogResponse?

    suspend fun syncWords(
        ids : List<String>
    ) : List<SyncWordItem>?

    suspend fun syncComments(
        ids : List<String>
    ) : List<SyncCommentItem>?

    suspend fun syncSyncTreeholes(
        ids : List<String>
    ) : List<SyncTreeholeItem>?

    suspend fun syncSyncNotifications(
        userId : String? = null,
        ids : List<String>
    ) : List<SyncNotificationItem>?

    suspend fun syncSyncBookmarks(
        userId: String? = null,
        ids : List<String>
    ) : List< SyncBookmarkItem>?

    suspend fun syncSyncLikes(
        userId: String? = null,
        ids : List<String>
    ) : List< SyncLikeItem>?

    suspend fun syncSyncFollows(
        userId: String? = null,
        ids : List<String>
    ) : List< SyncFollowItem>?

    suspend fun syncProfiles(profileIds : List<String>) : List<SyncProfileItem>?


}