package com.ljp.model

import kotlinx.datetime.Instant

//业务逻辑data model

data class WordTag(
    val id : String,
    val createTime : Instant,
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
data class WordCommentsResource internal constructor(
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
    val bookMarks : List<Profile>
) {
    constructor(word : Word,comments : List<Comment>,likes : List<Profile> ,bookMarks : List<Profile>) : this(
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
        bookMarks = bookMarks
    )
}
fun List<Word>.asWordCommentsResources(comments: List<Comment>,likes : List<Profile> ,bookMarks : List<Profile>) : List<WordCommentsResource> = map { WordCommentsResource(it,comments,likes,bookMarks) }

