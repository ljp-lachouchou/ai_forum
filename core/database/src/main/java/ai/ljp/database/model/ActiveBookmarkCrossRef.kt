package ai.ljp.database.model

import androidx.room.DatabaseView

@DatabaseView(
    viewName = "active_bookmarks",
    value = "SELECT `userId`, `postId` FROM bookmarks WHERE `deleted` = 0"
)
data class ActiveBookmarkCrossRef(
    val userId: String,
    val postId: String
)
