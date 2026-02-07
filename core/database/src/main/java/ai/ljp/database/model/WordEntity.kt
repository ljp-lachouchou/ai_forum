package ai.ljp.database.model

import androidx.annotation.StyleRes
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.ljp.model.Word
import com.ljp.model.WordTag
import kotlinx.datetime.Instant
import kotlin.reflect.KClass

interface  BaseEntity
@Entity(
    tableName = "words"
)
data class WordEntity(
    @PrimaryKey
    @ColumnInfo(name = "wordId")
    val wordId : String,
    @ColumnInfo(name = "authorId")
    val authorId : String,
    @ColumnInfo(name = "wordTags")
    val wordTags : List<WordTag>,
    @ColumnInfo(name = "wordUrl")
    val wordUrl : String,
    @ColumnInfo(name = "category")
    val category : String,
    @ColumnInfo(name = "createdAt")
    val createdAt : Instant,
    @ColumnInfo(name = "updatedAt")
    val updatedAt : Instant,
    @ColumnInfo(name = "wordName")
    val wordName : String,
    @ColumnInfo(name = "status")
    val status : String
) : BaseEntity
fun WordEntity.asExtraModel() = Word(
    wordId = wordId,
    authorId = authorId,
    wordTags = wordTags,
    wordUrl = wordUrl,
    category = category,
    createdAt = createdAt,
    updatedAt = updatedAt,
    status = status,
    wordName = wordName
)

