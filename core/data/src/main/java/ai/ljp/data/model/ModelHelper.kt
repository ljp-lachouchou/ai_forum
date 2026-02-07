package ai.ljp.data.model

import ai.ljp.database.model.CommentEntity
import ai.ljp.database.model.NotificationEntity
import com.ljp.model.Comment
import com.ljp.model.Notification


fun Comment.asDBModel() =
    CommentEntity(
        id = id,
        postId = postId,
        authorId = authorId,
        content = content,
        createdAt = createdAt
    )

fun Notification.asDBModel() =
    NotificationEntity(
        id = id,
        userId = userId,
        type = type,
        content = content,
        refType = refType,
        refId = refId,
        read = read,
        createdAt = createdAt
    )