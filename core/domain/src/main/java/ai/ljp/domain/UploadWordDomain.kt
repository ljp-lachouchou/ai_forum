package ai.ljp.domain

import ai.ljp.data.repository.InteractionWordRepository
import ai.ljp.data.repository.WordRepository
import ai.ljp.network.supabase.Actor
import androidx.paging.PagingData
import com.ljp.model.WordCommentsResource
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class UploadWordDomain @Inject constructor(
    private val wordRepository : WordRepository,
    private val interactionWordRepository: InteractionWordRepository,
    private val supabaseActor: Actor
){
    suspend operator fun invoke(fileName : String, bytes : ByteArray) : String? =
        supabaseActor.uploadAndGetUrl(
            bucketName = "mds",
            fileName = fileName,
            bytes = bytes,
        )
    fun observerAllWords() : Flow<PagingData<WordCommentsResource>> =
        interactionWordRepository.observerAllWords()
}