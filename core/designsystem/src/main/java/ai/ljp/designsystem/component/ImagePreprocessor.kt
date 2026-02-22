package ai.ljp.designsystem.component

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.io.ByteArrayOutputStream
import kotlin.math.max

data class PreparedImage(
    val bytes : ByteArray,
    val  extension : String = "jpg"
) {
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as PreparedImage

        if (!bytes.contentEquals(other.bytes)) return false
        if (extension != other.extension) return false

        return true
    }

    override fun hashCode(): Int {
        var result = bytes.contentHashCode()
        result = 31 * result + extension.hashCode()
        return result
    }
}
object ImagePreprocessor {
    private const val RAW_HEAD_LIMIT_BYTES = 20L * 1024 * 1024

    private const val TARGET_MAX_EDGE = 1920
    private const val TARGET_UPLOAD_BYTES = 2 * 1024 * 1024
    suspend fun prepare(context: Context,uri : Uri) : PreparedImage? =
        withContext(Dispatchers.IO) {
            val resolver = context.contentResolver
            val rawLen = resolver.openAssetFileDescriptor(uri,"r")?.use { afd->
                afd.length
            } ?: -1L
            if (rawLen > RAW_HEAD_LIMIT_BYTES) return@withContext null
            val bounds = BitmapFactory.Options().apply { inJustDecodeBounds = true }
            resolver.openInputStream(uri)?.use { input ->
                BitmapFactory.decodeStream(input, null, bounds)
            }
            if (bounds.outWidth <= 0 || bounds.outHeight <= 0) return@withContext null
            val sample = calculateInSampleSize(bounds.outWidth, bounds.outHeight, TARGET_MAX_EDGE)
            val decodeOpt = BitmapFactory.Options().apply {
                inSampleSize = sample
                inPreferredConfig = Bitmap.Config.ARGB_8888
            }
            val bitmap = resolver.openInputStream(uri)?.use { input ->
                BitmapFactory.decodeStream(input, null, decodeOpt)
            } ?: return@withContext null
            try {
                ByteArrayOutputStream().use { out ->
                    var quality = 85
                    bitmap.compress(Bitmap.CompressFormat.JPEG, quality, out)
                    while (out.size() > TARGET_UPLOAD_BYTES && quality > 70) {
                        out.reset()
                        quality -= 5
                        bitmap.compress(Bitmap.CompressFormat.JPEG, quality, out)
                    }
                    PreparedImage(bytes = out.toByteArray(), extension = "jpg")
                }
            } finally {
                bitmap.recycle()
            }
        }
    private fun calculateInSampleSize(width: Int, height: Int, maxEdge: Int): Int {
        val largest = max(width, height)
        if (largest <= maxEdge) return 1
        var sample = 1
        var cur = largest
        while (cur > maxEdge) {
            sample *= 2
            cur /= 2
        }
        return sample
    }
}