package ai.ljp.data.repository

import ai.ljp.data.Syncable
import androidx.paging.PagingData
import com.ljp.model.Word
import com.ljp.model.WordCommentsResource
import kotlinx.coroutines.flow.Flow

interface InteractionWordRepository  {
    fun observerAllWords() : Flow<PagingData<WordCommentsResource>>
}