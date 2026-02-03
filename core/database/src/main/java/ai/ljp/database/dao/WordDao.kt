package ai.ljp.database.dao

import ai.ljp.database.model.WordEntity
import androidx.paging.PagingSource
import androidx.room.Dao
import androidx.room.Query
import androidx.room.Upsert

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
        ORDER BY updatedAt DESC
    """)

    fun getWords() : PagingSource<Int, WordEntity>
}