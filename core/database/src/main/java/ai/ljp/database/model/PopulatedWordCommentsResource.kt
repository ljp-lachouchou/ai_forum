package ai.ljp.database.model

import androidx.room.Embedded
import androidx.room.Junction
import androidx.room.Relation
import com.ljp.model.WordCommentsResource

data class PopulatedWordCommentsResource(
    @Embedded
    val entity: WordEntity,
    @Relation(
        parentColumn = "wordId",
        entityColumn = "postId"
    )
    val comments : List<CommentEntity>,
    @Relation(
        parentColumn = "authorId",
        entityColumn = "profileId"
    )
    val profile : ProfileEntity,
    @Relation(
        parentColumn = "wordId",
        entityColumn = "profileId",
        associateBy = Junction(
            value = ActiveLikeCrossRef::class,
            parentColumn = "postId",
            entityColumn = "userId"
        )
    )
    val likes : List<ProfileEntity>,
    @Relation(
        parentColumn = "wordId",
        entityColumn = "profileId",
        associateBy = Junction(
            value = ActiveBookmarkCrossRef::class,
            parentColumn = "postId",
            entityColumn = "userId"
        )
    )
    val bookmarks : List<ProfileEntity>
)

fun PopulatedWordCommentsResource.asExtraModel() =
    WordCommentsResource(
        word = entity.asExtraModel(),
        comments = comments.map(CommentEntity::asExtraModel),
        likes = likes.map(ProfileEntity::asExtraModel),
        bookMarks = bookmarks.map(ProfileEntity::asExtraModel),
        author = profile.asExtraModel()
    )
