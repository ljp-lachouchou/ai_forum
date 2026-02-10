package com.ljp.model

import kotlinx.datetime.Instant

data class Comment(
    val id : String,
    val postId : String,
    val authorId : String,
    val content : String,
    val createdAt : Instant
)

data class CommentProfileResource(
    val commentId : String,
    val postId : String,
    val content: String,
    val createdAt: Instant,
    val author : Profile
)
fun Comment.mapToCommentProfileResource(author : Profile) =
    CommentProfileResource(
        commentId = id,
        postId = postId,
        content = content,
        createdAt = createdAt,
        author = author
    )
