package ai.ljp.database.util

import androidx.room.TypeConverter
import com.ljp.model.WordTag
import kotlinx.serialization.json.Json

class WordTagConverter {
    @TypeConverter
    fun wordTagListToString(value: List<WordTag>?): String? =
        value?.let { Json.encodeToString(it) }

    @TypeConverter
    fun stringToWordTagList(value: String?): List<WordTag>? =
        value?.let { Json.decodeFromString(it) }
}