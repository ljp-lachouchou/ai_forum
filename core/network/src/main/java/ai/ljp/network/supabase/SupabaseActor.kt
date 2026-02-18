package ai.ljp.network.supabase

import android.util.Log
import io.github.jan.supabase.SupabaseClient
import io.github.jan.supabase.storage.storage
import javax.inject.Inject

class SupabaseActor @Inject constructor(
    private val supabaseClient: SupabaseClient
) : Actor {
    override suspend fun uploadAndGetUrl(
        bucketName : String,
        fileName : String,
        bytes : ByteArray
    ) : String? = try {
        val bucket = supabaseClient.storage.from(bucketName)
        bucket.upload(path = fileName, data = bytes) {
            upsert = true
        }
        val publicUrl = bucket.publicUrl(fileName)
        publicUrl
    }catch (e : Exception) {
        Log.e("SupabaseActor Exception","$e")
        null
    }
}