package ai.ljp.domain

import ai.ljp.data.repository.ProfileRepository
import ai.ljp.network.supabase.Actor
import javax.inject.Inject

class UploadProfileDomain @Inject constructor(
    private val supabaseActor: Actor,
    private val profileRepository: ProfileRepository
){
    suspend operator  fun invoke(fileName : String, bytes : ByteArray) : String? =
        supabaseActor.uploadAndGetUrl(
            bucketName = "images",
            fileName = fileName,
            bytes = bytes,
        )
}