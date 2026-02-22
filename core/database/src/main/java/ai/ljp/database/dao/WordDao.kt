package ai.ljp.database.dao

import ai.ljp.database.model.PopulatedWordCommentsResource
import ai.ljp.database.model.WordEntity
import androidx.paging.PagingSource
import androidx.room.Dao
import androidx.room.Query
import androidx.room.Transaction
import androidx.room.Upsert
import kotlinx.coroutines.flow.Flow

@Dao
interface WordDao {
    @Upsert
    suspend fun upsertAll(words : List<WordEntity>)

    @Query(
        """
            DELETE FROM words 
            WHERE `wordId` = :wordId
        """
    )
    suspend fun deleteWord(wordId : String)
    @Query("""
        SELECT * FROM words 
        WHERE `status` = 'published'
        ORDER BY updatedAt DESC
    """)

    fun getWords() : PagingSource<Int, WordEntity>

    @Query("""
        SELECT `wordId` FROM words 
        WHERE `authorId` = :profileId and 
        `status` = 'published'
    """)
    fun getWordIds(profileId : String) : List<String>

    @Transaction
    @Query("""
        SELECT 
    w.*,
    -- 处理作者 Profile
    (SELECT '{"profileId":"' || p.profileId || 
             '","userName":"' || IFNULL(p.userName, '') || 
             '","avatarUrl":"' || IFNULL(p.avatarUrl, '') || 
             '","role":"' || p.role || '"}'
     FROM profiles AS p 
     WHERE p.profileId = w.authorId) AS profile,
    
    -- 1. 聚合评论 (Comments)
    (SELECT '[' || GROUP_CONCAT(
        '{"commentId":"' || c.commentId || '","content":"' || c.content || '"}'
    ) || ']' 
     FROM comments AS c 
     WHERE c.postId = w.wordId) AS comments,

    -- 2. 聚合点赞用户 (Likes) - 过滤 isDeleted = 0
    (SELECT '[' || GROUP_CONCAT(
        '{"profileId":"' || lp.profileId || '","name":"' || lp.userName || '"}'
    ) || ']'
     FROM likes AS l
     JOIN profiles AS lp ON l.userId = lp.profileId
     WHERE l.postId = w.wordId AND l.deleted = 0) AS likes,

    -- 3. 聚合收藏用户 (Bookmarks) - 过滤 isDeleted = 0
    (SELECT '[' || GROUP_CONCAT(
        '{"profileId":"' || bp.profileId || '","name":"' || bp.userName || '"}'
    ) || ']'
     FROM bookmarks AS b
     JOIN profiles AS bp ON b.userId = bp.profileId
     WHERE b.postId = w.wordId AND b.deleted = 0) AS bookmarks

FROM words AS w
LEFT JOIN profiles AS p ON w.authorId = p.profileId
WHERE w.status = 'published'
ORDER BY w.createdAt DESC
        """)
    fun getPopulatedWordResources(): PagingSource<Int, PopulatedWordCommentsResource>

    @Transaction
    @Query("""
        SELECT 
    w.*,
    -- 处理作者 Profile
    (SELECT '{"profileId":"' || p.profileId || 
             '","userName":"' || IFNULL(p.userName, '') || 
             '","avatarUrl":"' || IFNULL(p.avatarUrl, '') || 
             '","role":"' || p.role || '"}'
     FROM profiles AS p 
     WHERE p.profileId = w.authorId) AS profile,
    
    -- 1. 聚合评论 (Comments)
    (SELECT '[' || GROUP_CONCAT(
        '{"commentId":"' || c.commentId || '","content":"' || c.content || '"}'
    ) || ']' 
     FROM comments AS c 
     WHERE c.postId = w.wordId) AS comments,

    -- 2. 聚合点赞用户 (Likes) - 过滤 isDeleted = 0
    (SELECT '[' || GROUP_CONCAT(
        '{"profileId":"' || lp.profileId || '","name":"' || lp.userName || '"}'
    ) || ']'
     FROM likes AS l
     JOIN profiles AS lp ON l.userId = lp.profileId
     WHERE l.postId = w.wordId AND l.deleted = 0) AS likes,

    -- 3. 聚合收藏用户 (Bookmarks) - 过滤 isDeleted = 0
    (SELECT '[' || GROUP_CONCAT(
        '{"profileId":"' || bp.profileId || '","name":"' || bp.userName || '"}'
    ) || ']'
     FROM bookmarks AS b
     JOIN profiles AS bp ON b.userId = bp.profileId
     WHERE b.postId = w.wordId AND b.deleted = 0) AS bookmarks

FROM words AS w
LEFT JOIN profiles AS p ON w.authorId = p.profileId
WHERE w.authorId = :profileId AND w.status = 'published'
ORDER BY w.createdAt DESC
    """)
    fun getSelfWords(profileId : String) : PagingSource<Int, PopulatedWordCommentsResource>

    @Transaction
    @Query("""
        SELECT 
    w.*,
    -- 处理作者 Profile
    (SELECT '{"profileId":"' || p.profileId || 
             '","userName":"' || IFNULL(p.userName, '') || 
             '","avatarUrl":"' || IFNULL(p.avatarUrl, '') || 
             '","role":"' || p.role || '"}'
     FROM profiles AS p 
     WHERE p.profileId = w.authorId) AS profile,
    
    -- 1. 聚合评论 (Comments)
    (SELECT '[' || GROUP_CONCAT(
        '{"commentId":"' || c.commentId || '","content":"' || c.content || '"}'
    ) || ']' 
     FROM comments AS c 
     WHERE c.postId = w.wordId) AS comments,

    -- 2. 聚合点赞用户 (Likes) - 过滤 isDeleted = 0
    (SELECT '[' || GROUP_CONCAT(
        '{"profileId":"' || lp.profileId || '","name":"' || lp.userName || '"}'
    ) || ']'
     FROM likes AS l
     JOIN profiles AS lp ON l.userId = lp.profileId
     WHERE l.postId = w.wordId AND l.deleted = 0) AS likes,

    -- 3. 聚合收藏用户 (Bookmarks) - 过滤 isDeleted = 0
    (SELECT '[' || GROUP_CONCAT(
        '{"profileId":"' || bp.profileId || '","name":"' || bp.userName || '"}'
    ) || ']'
     FROM bookmarks AS b
     JOIN profiles AS bp ON b.userId = bp.profileId
     WHERE b.postId = w.wordId AND b.deleted = 0) AS bookmarks

FROM words AS w
LEFT JOIN profiles AS p ON w.authorId = p.profileId
WHERE w.wordId = :wordId AND w.status = 'published' 
        LIMIT 1
    """)
    fun getPost(wordId : String) : Flow<PopulatedWordCommentsResource>

    @Query(
        value = """
            DELETE FROM words
            WHERE `wordId` in (:ids)
        """,
    )
    suspend fun deleteAll(ids : List<String>)
    @Transaction
    @Query("""
    SELECT 
    w.*,
    -- 处理作者 Profile
    (SELECT '{"profileId":"' || p.profileId || 
             '","userName":"' || IFNULL(p.userName, '') || 
             '","avatarUrl":"' || IFNULL(p.avatarUrl, '') || 
             '","role":"' || p.role || '"}'
     FROM profiles AS p 
     WHERE p.profileId = w.authorId) AS profile,
    
    -- 1. 聚合评论 (Comments)
    (SELECT '[' || GROUP_CONCAT(
        '{"commentId":"' || c.commentId || '","content":"' || c.content || '"}'
    ) || ']' 
     FROM comments AS c 
     WHERE c.postId = w.wordId) AS comments,

    -- 2. 聚合点赞用户 (Likes) - 过滤 isDeleted = 0
    (SELECT '[' || GROUP_CONCAT(
        '{"profileId":"' || lp.profileId || '","name":"' || lp.userName || '"}'
    ) || ']'
     FROM likes AS l
     JOIN profiles AS lp ON l.userId = lp.profileId
     WHERE l.postId = w.wordId AND l.deleted = 0) AS likes,

    -- 3. 聚合收藏用户 (Bookmarks) - 过滤 isDeleted = 0
    (SELECT '[' || GROUP_CONCAT(
        '{"profileId":"' || bp.profileId || '","name":"' || bp.userName || '"}'
    ) || ']'
     FROM bookmarks AS b
     JOIN profiles AS bp ON b.userId = bp.profileId
     WHERE b.postId = w.wordId AND b.deleted = 0) AS bookmarks

FROM words AS w
LEFT JOIN profiles AS p ON w.authorId = p.profileId
WHERE w.wordId IN (:wordIds) AND w.status = 'published'
ORDER BY w.createdAt DESC
""")
    fun getPostByIds(wordIds : List<String>) : PagingSource<Int, PopulatedWordCommentsResource>
}