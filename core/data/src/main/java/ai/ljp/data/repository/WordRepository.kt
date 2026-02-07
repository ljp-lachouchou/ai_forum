package ai.ljp.data.repository

import ai.ljp.data.Syncable
import ai.ljp.data.model.AIPost
import ai.ljp.network.model.AIAssistPostResponse
import ai.ljp.network.model.WordUpdateRequesst
import androidx.paging.PagingData
import com.ljp.model.Word
import com.ljp.model.WordTag
import kotlinx.coroutines.flow.Flow

interface WordRepository : Syncable {
    suspend fun createWord(
        wordUrl : String,
        category : String,
        tags : List<WordTag>,
        wordName : String
    ) : Boolean
    suspend fun updateWord(
        wordId : String,
        wordUpdateRequesst: WordUpdateRequesst
    )
    suspend fun deleteWord(wordId : String)


    fun getWords() : Flow<PagingData<Word>>

    suspend fun aiAssistPost(
        content : String
    ) : AIPost
}