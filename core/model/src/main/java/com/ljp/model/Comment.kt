package com.ljp.model

import kotlinx.datetime.Instant

data class Comment(
    val id : String,
    val postId : String,
    val authorId : String,
    val content : String,
    val createdAt : Instant
)
