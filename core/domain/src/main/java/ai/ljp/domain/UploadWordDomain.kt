package ai.ljp.domain

import ai.ljp.data.repository.WordRepository
import ai.ljp.network.supabase.Actor
import javax.inject.Inject

class UploadWordDomain @Inject constructor(
    private val wordRepository : WordRepository,
    private val supabaseActor: Actor
){
    suspend operator fun invoke(fileName : String, bytes : ByteArray) : String? =
        supabaseActor.uploadAndGetUrl(
            bucketName = "mds",
            fileName = fileName,
            bytes = bytes,
        )
}