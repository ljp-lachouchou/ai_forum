package ai.ljp.data.repository

import ai.ljp.data.Syncable
import androidx.paging.PagingData
import com.ljp.model.Word
import kotlinx.coroutines.flow.Flow

interface WordRepository : Syncable {
    suspend fun upsertAll(words : List<Word>)

    suspend fun deleteWord(wordId : String)


    fun getWords() : Flow<PagingData<Word>>
}