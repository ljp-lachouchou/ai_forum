package com.ljp.model

import kotlinx.datetime.Instant
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

//业务逻辑data model
@Serializable
data class WordTag(
    val id : String,
    @SerialName("create_time")
    val createTime : Long,
    @SerialName("display_content")
    val displayContent : String
)
data class Word(
    val wordId : String,
    val authorId : String,
    val wordTags : List<WordTag>,
    val wordUrl : String,
    val category : String,
    val status : String,
    val createdAt : Instant,
    val updatedAt : Instant,
    val wordName : String
)

/**
 * comments
 * words
 * likes
 * bookMarks
 */
data class WordCommentsResource(
    val wordId: String,
    val authorId : String,
    val wordName : String,
    val wordTags : List<WordTag>,
    val wordUrl : String,
    val status: String,
    val category : String,
    val createdAt : Instant,
    val updatedAt : Instant = createdAt,
    val comments : List<Comment>,
    val likes : List<Profile>,
    val bookMarks : List<Profile>,
    val author: Profile
) {
    constructor(word : Word,comments : List<Comment>,likes : List<Profile> ,bookMarks : List<Profile>,author: Profile) : this(
        wordId = word.wordId,
        authorId = word.authorId,
        wordName = word.wordName,
        wordTags = word.wordTags,
        wordUrl = word.wordUrl,
        category = word.category,
        createdAt = word.createdAt,
        updatedAt = word.updatedAt,
        comments = comments,
        status = word.status,
        likes = likes,
        bookMarks = bookMarks,
        author = author
    )
}
fun List<Word>.asWordCommentsResources(comments: List<Comment>,likes : List<Profile> ,bookMarks : List<Profile>,profile: Profile) : List<WordCommentsResource> = map { WordCommentsResource(it,comments,likes,bookMarks,profile) }

