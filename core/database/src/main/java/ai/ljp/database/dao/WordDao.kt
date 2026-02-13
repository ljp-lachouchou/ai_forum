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
        SELECT * FROM words 
        WHERE `status` = 'published' 
        ORDER BY createdAt DESC
        """)
    fun getPopulatedWordResources(): PagingSource<Int, PopulatedWordCommentsResource>

    @Transaction
    @Query("""
        SELECT * FROM words 
        WHERE `authorId` = :profileId and `status` = 'published'
        ORDER BY createdAt DESC
    """)
    fun getSelfWords(profileId : String) : PagingSource<Int, PopulatedWordCommentsResource>

    @Transaction
    @Query("""
        SELECT * FROM words 
        WHERE `wordId` = :wordId and `status` = 'published'
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
        SELECT * FROM words 
        WHERE `wordId` IN (:wordIds) and `status` = 'published' 
        ORDER BY `createdAt` DESC
    """)
    fun getPostByIds(wordIds : List<String>) : PagingSource<Int, PopulatedWordCommentsResource>
}