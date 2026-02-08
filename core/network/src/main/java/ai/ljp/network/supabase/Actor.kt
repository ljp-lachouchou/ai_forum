package ai.ljp.network.supabase

interface Actor {
    suspend fun uploadAndGetUrl(
        bucketName : String,
        fileName : String,
        bytes : ByteArray
    ) : String?
}