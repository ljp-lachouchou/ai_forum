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
    val category : String,
    val createdAt : Instant,
    val updatedAt : Instant,
    val comments : List<Comment>,
    val likes : Int = 0,
    val bookMarks : Int = 0
) {
    constructor(word : Word,comments : List<Comment>,like: Int,bookMark : Int) : this(
        wordId = word.wordId,
        authorId = word.authorId,
        wordName = word.wordName,
        wordTags = word.wordTags,
        wordUrl = word.wordUrl,
        category = word.category,
        createdAt = word.createdAt,
        updatedAt = word.updatedAt,
        comments = comments,
        likes = like,
        bookMarks = bookMark
    )
}
fun List<Word>.asWordCommentsResources(comments: List<Comment>,like : Int,bookMark : Int) : List<WordCommentsResource> = map { WordCommentsResource(it,comments,like,bookMark) }

