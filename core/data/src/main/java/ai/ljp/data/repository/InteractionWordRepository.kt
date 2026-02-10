package ai.ljp.data.repository

import ai.ljp.data.model.AISearch
import androidx.paging.PagingData
import com.ljp.model.WordCommentsResource
import kotlinx.coroutines.flow.Flow

interface InteractionWordRepository  {
    fun observerAllWords() : Flow<PagingData<WordCommentsResource>>
    suspend fun aiSearch(
        query : String
    ) : AISearch
    fun getSelfWords(profileId : String) : Flow<PagingData<WordCommentsResource>>

    fun getPost(wordId : String) : Flow<WordCommentsResource>
}