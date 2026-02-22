package ai.ljp.database.model

import androidx.room.DatabaseView

@DatabaseView(
    viewName = "active_likes",
    value = "SELECT `userId`, `postId` FROM likes WHERE `deleted` = 0"
)
data class ActiveLikeCrossRef(
    val userId: String,
    val postId: String
)
