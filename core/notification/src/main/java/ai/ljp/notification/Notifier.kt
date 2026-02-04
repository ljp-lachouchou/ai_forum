package ai.ljp.notification

import com.ljp.model.Word

interface Notifier {
    fun postPostsNotifications(
        words : List<Word>
    )
}