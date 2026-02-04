package ai.ljp.network.ktor

import ai.ljp.network.AIForumNetworkDataSource
import ai.ljp.network.model.AIAssistPostResponse
import ai.ljp.network.model.AISearchResponse
import ai.ljp.network.model.ChangelogItem
import ai.ljp.network.model.CollectResponse
import ai.ljp.network.model.CommentCreateResponse
import ai.ljp.network.model.CommentItem
import ai.ljp.network.model.FollowCreateResponse
import ai.ljp.network.model.FollowItem
import ai.ljp.network.model.LikeResponse
import ai.ljp.network.model.LoginResponse
import ai.ljp.network.model.NotificationCreateResponse
import ai.ljp.network.model.NotificationItem
import ai.ljp.network.model.NotificationReadAllResponse
import ai.ljp.network.model.NotificationReadResponse
import ai.ljp.network.model.PersonaUpdateAvailableResponse
import ai.ljp.network.model.PersonaUpdateStatsResponse
import ai.ljp.network.model.ProfileResponse
import ai.ljp.network.model.RegisterResponse
import ai.ljp.network.model.ReportCreateResponse
import ai.ljp.network.model.SyncBookmarkItem
import ai.ljp.network.model.SyncCommentItem
import ai.ljp.network.model.SyncLikeItem
import ai.ljp.network.model.SyncNotificationItem
import ai.ljp.network.model.SyncTreeholeItem
import ai.ljp.network.model.SyncWordItem
import ai.ljp.network.model.TreeholeAiReplyResponse
import ai.ljp.network.model.TreeholeCreateResponse
import ai.ljp.network.model.TreeholeItem
import ai.ljp.network.model.UserLikeItem
import ai.ljp.network.model.WordDetailResponse
import ai.ljp.network.model.WordFeedItem
import ai.ljp.network.model.WordUpdateRequesst
import ai.ljp.network.model.simApiMapOf
import android.util.Log
import com.ljp.model.WordTag
import io.ljp.simapi.ApiClient
import io.ljp.simapi.ResultWrapper
import io.ljp.simapi.apiClient
import io.ljp.simapi.apiRequest
import io.ljp.simapi.util.simApiMapOf
import javax.inject.Inject


fun <T> ResultWrapper<T>.getOrNull() =
    when(this) {
        is ResultWrapper.Success -> data
        is ResultWrapper.Error -> {
            Log.e("RW getOrNull throwable","throwable is ",cause)
            null
        }
    }
class KtorAIForumNetwork @Inject constructor(
    val client : ApiClient
) : AIForumNetworkDataSource {
    override suspend fun login(email: String, password: String) : LoginResponse? =
        client.apiClient<LoginResponse?> {
            post<LoginResponse?> {
                apiRequest("/api/v1/login") {
                    body = mapOf("email" to email,"password" to password)
                }
            }.getOrNull()
        }

    override suspend fun register(
        email: String,
        password: String
    ): RegisterResponse? =
        client.apiClient<RegisterResponse?> {
            post<RegisterResponse?> {
                apiRequest("/api/v1/register") {
                    body = mapOf("email" to email,"password" to password)
                }
            }.getOrNull()
        }

    override suspend fun getProfile(profileId: String): ProfileResponse? =
        client.apiClient<ProfileResponse?> {
            get<ProfileResponse?> {
                apiRequest("/api/v1/profile/get_profile") {
                    params = mapOf("id" to profileId,)
                }
            }.getOrNull()
        }

    override suspend fun updatedProfile(
        profileId: String,
        userName: String?,
        avatarUrl: String?,
        bio: String?
    ): ProfileResponse? =
        client.apiClient<ProfileResponse?> {
            put<ProfileResponse?> {
                apiRequest("/api/v1/profile/update_profile") {
                    body = simApiMapOf(
                        "id" to profileId,
                        "username" to userName,
                        "avatar_url" to avatarUrl,
                        "bio" to bio
                    )
                }
            }.getOrNull()
        }

    override suspend fun updateStats(
        userId: String,
        category: String,
        tag: List<String>,
        duration: Int
    ): PersonaUpdateStatsResponse? =
        client.apiClient<PersonaUpdateStatsResponse?> {
            put<PersonaUpdateStatsResponse?> {
                apiRequest("/api/v1/persona/update_stats") {
                    body = simApiMapOf(
                        "user_id" to userId,
                        "category" to category,
                        "tags" to tag,
                        "duration" to duration
                    )
                }
            }.getOrNull()
        }

    override suspend fun updateAvailable(userId: String): PersonaUpdateAvailableResponse? =
        client.apiClient<PersonaUpdateAvailableResponse? > {
            post<PersonaUpdateAvailableResponse? > {
                apiRequest("/api/v1/persona/update_available") {
                    body = simApiMapOf("id" to userId)
                }
            }.getOrNull()
        }

    override suspend fun createWord(
        authorId: String,
        wordUrl: String,
        category: String,
        tags: List<WordTag>,
        wordName: String
    ): WordDetailResponse?  =
        client.apiClient<WordDetailResponse?> {
            post<WordDetailResponse?> {
                apiRequest("/api/v1/words") {
                    body = simApiMapOf(
                        "author_id" to authorId,
                        "word_url" to wordUrl,
                        "category" to category,
                        "tags" to tags,
                        "word_name" to wordName
                    )

                }
            }.getOrNull()
        }

    override suspend fun updateWord(
        wordId: String,
        authorId: String,
        wordUpdateRequesst: WordUpdateRequesst
    )  =
        client.apiClient {
            put<Unit> {
                apiRequest("/api/v1/words/$wordId") {
                    params = simApiMapOf(
                        "author_id" to authorId,
                    )
                    body = wordUpdateRequesst.simApiMapOf()
                }
            }
        }

    override suspend fun submitWord(wordId: String, authorId: String) =
        client.apiClient {
            post<Unit> {
                apiRequest("/api/v1/words/$wordId/submit") {
                    params = simApiMapOf(
                        "author_id" to authorId
                    )
                }
            }
        }

    override suspend fun publishWord(wordId: String, adminId: String) =
        client.apiClient {
            post<Unit> {
                apiRequest("/api/v1/words/$wordId/publish") {
                    params = simApiMapOf(
                        "admin_id" to adminId
                    )
                }
            }
        }

    override suspend fun rejectWord(
        wordId: String,
        adminId: String,
        reson: String
    )  =
        client.apiClient {
            post<Unit> {
                apiRequest("/api/v1/words/$wordId/reject") {
                    params = simApiMapOf(
                        "admin_id" to adminId,
                        "reson" to reson
                    )
                }
            }
        }

    override suspend fun archiveWord(wordId: String, adminId: String) =
        client.apiClient {
            post<Unit> {
                apiRequest("/api/v1/words/$wordId/archive") {
                    body = simApiMapOf(
                        "admin_id" to adminId
                    )
                }
            }
        }

    override suspend fun deleteWord(wordId: String)  =
        client.apiClient {
            delete<Unit> {
                apiRequest("/api/v1/words/$wordId")
            }
        }

    override suspend fun getWord(wordId: String): WordDetailResponse? =
        client.apiClient<WordDetailResponse?> {
            get<WordDetailResponse?> {
                apiRequest("/api/v1/words/$wordId")
            }.getOrNull()
        }

    override suspend fun getWords(
        mode: String,
        category: String?,
        userId: String?,
        limit: Int?
    ): List<WordFeedItem>? =
        client.apiClient<List<WordFeedItem>?> {
            get<List<WordFeedItem>?> {
                apiRequest("/api/v1/words/feeds") {
                    params = simApiMapOf(
                        "mode" to mode,
                        "category" to category,
                        "user_id" to userId,
                        "limit" to limit
                    )
                }
            }.getOrNull()
        }

    override suspend fun aiSearch(
        userId: String,
        query: String
    ): AISearchResponse? =
        client.apiClient<AISearchResponse?> {
            post<AISearchResponse?> {
                apiRequest("/api/v1/ai/search") {
                    body = simApiMapOf(
                        "u_id" to userId,
                        "query" to query
                    )
                }
            }.getOrNull()
        }

    override suspend fun aiReview(postId: String) =
        client.apiClient {
            post<Unit> {
                apiRequest("/api/v1/ai/review") {
                    params = simApiMapOf("id" to postId)
                }
            }
        }

    override suspend fun aiAssistPost(content: String): AIAssistPostResponse? =
        client.apiClient<AIAssistPostResponse?> {
            post<AIAssistPostResponse?> {
                apiRequest("/api/v1/ai/assist/post") {
                    body = simApiMapOf("content" to content)
                }
            }.getOrNull()
        }

    override suspend fun createComment(
        postId: String,
        authorId: String,
        content: String
    ): CommentCreateResponse? =
        client.apiClient<CommentCreateResponse?> {
            post<CommentCreateResponse?> {
                apiRequest(" /api/v1/posts/$postId/comments") {
                    body = simApiMapOf("author_id" to authorId,"content" to content)
                }
            }.getOrNull()
        }

    override suspend fun getComments(postId: String): List<CommentItem>? =
        client.apiClient<List<CommentItem>?> {
            get<List<CommentItem>?> {
                apiRequest(" /api/v1/posts/$postId/comments")
            }.getOrNull()
        }

    override suspend fun deleteComment(commentId: String, authorId: String) =
        client.apiClient {
            delete<Unit> {
                apiRequest("/api/v1/comments/$commentId") {
                    body = simApiMapOf(
                        "author_id" to authorId
                    )
                }
            }
        }

    override suspend fun toggleLike(
        postId: String,
        userId: String
    ): LikeResponse? =
        client.apiClient<LikeResponse?> {
            post<LikeResponse?> {
                apiRequest("/api/v1/posts/$postId/like") {
                    body = simApiMapOf("user_id" to userId)
                }
            }.getOrNull()
        }

    override suspend fun toggleBookmark(
        postId: String,
        userId: String
    ): CollectResponse? =
        client.apiClient<CollectResponse?> {
            post<CollectResponse?> {
                apiRequest("/api/v1/posts/$postId/collect") {
                    body = simApiMapOf("user_id" to userId)
                }
            }.getOrNull()
        }

    override suspend fun getLikes(userId: String): List<UserLikeItem>? =
        client.apiClient<List<UserLikeItem>?> {
            get<List<UserLikeItem>?> {
                apiRequest("/api/v1/user/likes") {
                    params = simApiMapOf("user_id" to userId)
                }
            }.getOrNull()
        }

    override suspend fun createTreehole(
        authorId: String,
        content: String,
        isAnonymous: Boolean
    ): TreeholeCreateResponse?=
        client.apiClient<TreeholeCreateResponse?> {
            post<TreeholeCreateResponse?> {
                apiRequest("/api/v1/treehole") {
                    body = simApiMapOf(
                        "author_id" to authorId,
                        "content" to content,
                        "is_anonymous" to isAnonymous
                    )
                }
            }.getOrNull()
        }

    override suspend fun getTreeholes(
        limit: Int?,
        offset: Int?,
        includeAI: Boolean?
    ): List<TreeholeItem>? =
        client.apiClient<List<TreeholeItem>?> {
            get<List<TreeholeItem>?> {
                apiRequest("/api/v1/treehole/stream") {
                    params = simApiMapOf(
                        "limit" to limit,
                        "offset" to offset,
                        "include_ai" to includeAI
                    )
                }
            }.getOrNull()
        }

    override suspend fun treeholeAIReply(
        treeholeId: String,
        content: String
    ): TreeholeAiReplyResponse? =
        client.apiClient<TreeholeAiReplyResponse?> {
            post<TreeholeAiReplyResponse?> {
                apiRequest("/api/v1/treehole/$treeholeId/ai_reply") {
                    body = simApiMapOf(
                        "content" to content
                    )
                }
            }.getOrNull()
        }

    override suspend fun createNotification(
        userId: String,
        type: String,
        content: String,
        refType: String?,
        refId: String?
    ): NotificationCreateResponse? =
        client.apiClient<NotificationCreateResponse?> {
            post<NotificationCreateResponse?> {
                apiRequest("/api/v1/notifications") {
                    body = simApiMapOf(
                        "user_id" to userId,
                        "type" to type,
                        "content" to content,
                        "ref_type" to refType,
                        "ref_id" to refId
                    )
                }
            }.getOrNull()
        }

    override suspend fun getNotifications(
        userId: String,
        unreadOnly: Boolean?,
        limit: Int?
    ): List<NotificationItem>? =
        client.apiClient<List<NotificationItem>?> {
              get<List<NotificationItem>?> {
                  apiRequest("/api/v1/notifications") {
                      params = simApiMapOf(
                          "user_id" to userId,
                          "unread_only" to unreadOnly,
                          "limit" to limit
                      )
                  }
              }.getOrNull()
        }

    override suspend fun readNotification(
        userId: String,
        notificationIds: List<String>
    ): NotificationReadResponse? =
        client.apiClient<NotificationReadResponse?> {
            post<NotificationReadResponse?> {
                apiRequest("/api/v1/notifications/read") {
                    body = simApiMapOf(
                        "user_id" to userId,
                        "notification_ids" to notificationIds
                    )
                }
            }.getOrNull()
        }

    override suspend fun readAllNotification(userId: String): NotificationReadAllResponse? =
        client.apiClient<NotificationReadAllResponse?> {
            post<NotificationReadAllResponse?> {
                apiRequest("/api/v1/notifications/read_all") {
                    body = simApiMapOf(
                        "user_id" to userId
                    )
                }
            }.getOrNull()
        }

    override suspend fun createFollow(
        userId: String,
        followId: String
    ): FollowCreateResponse? =
        client.apiClient<FollowCreateResponse??> {
            post<FollowCreateResponse?> {
                apiRequest("/api/v1/follows") {
                    body = simApiMapOf(
                        "user_id" to userId,
                        "follow_id" to followId
                    )
                }
            }.getOrNull()
        }

    override suspend fun deleteFollow(userId: String, followId: String) =
        client.apiClient {
            delete<Unit> {
                apiRequest("/api/v1/follows") {
                    body = simApiMapOf("user_id" to userId,
                        "follow_id" to followId)
                }
            }
        }

    override suspend fun getFollows(userId: String): List<FollowItem>?  =
        client.apiClient<List<FollowItem>?> {
            get<List<FollowItem>?> {
                apiRequest("/api/v1/follows") {
                    params = simApiMapOf("user_id" to userId)
                }
            }.getOrNull()
        }

    override suspend fun createReport(
        reporterId: String?,
        targetType: String,
        targetId: String,
        reson: String
    ): ReportCreateResponse? =
        client.apiClient<ReportCreateResponse?> {
            post<ReportCreateResponse?> {
                apiRequest("/api/v1/reports") {
                    body = simApiMapOf(
                        "reporter_id" to reporterId,
                        "target_type" to targetType,
                        "target_id" to targetId,
                        "reason" to reson
                    )
                }
            }.getOrNull()
        }

    override suspend fun getChangelogs(
        since: Int,
        limit: Int?
    ): List<ChangelogItem>? =
        client.apiClient<List<ChangelogItem>?> {
            get<List<ChangelogItem>?> {
                apiRequest("/api/v1/sync/changelog") {
                    params = simApiMapOf(
                        "since" to since,
                        "limit" to limit
                    )
                }
            }.getOrNull()
        }

    override suspend fun syncWords(ids: List<String>): List<SyncWordItem>?  =
        client.apiClient<List<SyncWordItem>?> {
            post<List<SyncWordItem>?> {
                apiRequest("/api/v1/sync/words") {
                    body = simApiMapOf(
                        "ids" to ids
                    )
                }
            }.getOrNull()
        }

    override suspend fun syncComments(ids: List<String>): List<SyncCommentItem>? =
        client.apiClient<List<SyncCommentItem>?> {
            post<List<SyncCommentItem>?> {
                apiRequest("/api/v1/sync/comments") {
                    body = simApiMapOf(
                        "ids" to ids
                    )
                }
            }.getOrNull()
        }

    override suspend fun syncSyncTreeholes(ids: List<String>): List<SyncTreeholeItem>? =
        client.apiClient<List<SyncTreeholeItem>?> {
            post<List<SyncTreeholeItem>?> {
                apiRequest("/api/v1/sync/treeholes") {
                    body = simApiMapOf(
                        "ids" to ids
                    )
                }
            }.getOrNull()
        }

    override suspend fun syncSyncNotifications(userId: String, ids: List<String>): List<SyncNotificationItem>? =
        client.apiClient<List<SyncNotificationItem>?> {
            post<List<SyncNotificationItem>?> {
                apiRequest("/api/v1/sync/notifications") {
                    body = simApiMapOf(
                        "ids" to ids,
                        "user_id" to userId
                    )
                }
            }.getOrNull()
        }

    override suspend fun syncSyncBookmarks(userId: String, ids: List<String>): List<SyncBookmarkItem>?  =
        client.apiClient<List<SyncBookmarkItem>?> {
            post<List<SyncBookmarkItem>?> {
                apiRequest("/api/v1/sync/bookmarks") {
                    body = simApiMapOf(
                        "ids" to ids,
                        "user_id" to userId
                    )
                }
            }.getOrNull()
        }
    override suspend fun syncSyncLikes(userId: String, ids: List<String>): List<SyncLikeItem>?  =
        client.apiClient<List<SyncLikeItem>?> {
            post<List<SyncLikeItem>?> {
                apiRequest("/api/v1/sync/likes") {
                    body = simApiMapOf(
                        "ids" to ids,
                        "user_id" to userId
                    )
                }
            }.getOrNull()
        }


}