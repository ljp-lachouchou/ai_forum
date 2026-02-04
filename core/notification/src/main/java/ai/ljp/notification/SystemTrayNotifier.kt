package ai.ljp.notification

import android.Manifest
import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager.PERMISSION_GRANTED
import android.os.Build
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationCompat.InboxStyle
import androidx.core.app.NotificationManagerCompat
import androidx.core.content.ContextCompat.checkSelfPermission
import androidx.core.net.toUri
import com.ljp.model.Word
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject
import javax.inject.Singleton
private const val MAX_NUM_NOTIFICATIONS = 5
private const val WORDS_NOTIFICATION_REQUEST_CODE = 0
private const val WORDS_NOTIFICATION_SUMMARY_ID = 1
private const val WORDS_NOTIFICATION_CHANNEL_ID = "notification_channel_id"
private const val WORDS_NOTIFICATION_GROUP = "WORDS_NOTIFICATIONS"

private const val TARGET_ACTIVITY_NAME = "ai.ljp.aiforum.MainActivity"
private const val DEEP_LINK_SCHEME_AND_HOST = "https://www.apps.ai.forum.ljp.com"
private const val DEEP_LINK_HOME_PATH = "home"
private const val DEEP_LINK_BASE_PATH = "$DEEP_LINK_SCHEME_AND_HOST/$DEEP_LINK_HOME_PATH"
@Singleton
class SystemTrayNotifier @Inject constructor(
    @ApplicationContext private val context: Context
) : Notifier{
    override fun postPostsNotifications(words: List<Word>) {
        with(context) {
            if (checkSelfPermission(this, Manifest.permission.POST_NOTIFICATIONS) != PERMISSION_GRANTED) {
                return
            }
            val truncatedWords = words.take(MAX_NUM_NOTIFICATIONS)
            val wordsNotification = truncatedWords.map { word ->
                createWordsNotification {
                    setSmallIcon(R.drawable.core_notifications_ic_aiforum_notification)
                        .setContentTitle(word.wordName)
                        .setGroup(WORDS_NOTIFICATION_GROUP)
                        .setContentIntent(wordsPendingIntent(word))
                        .setAutoCancel(true)
                }
            }
            val summaryNotification = createWordsNotification {
                val title = getString(
                    R.string.core_notifications_words_summary_notification_title,
                    truncatedWords.size
                )
                setContentTitle(title)
                    .setGroup(WORDS_NOTIFICATION_GROUP)
                    .setGroupSummary(true)
                    .setStyle(wordsNotificationStyle(truncatedWords,title))
                    .setSmallIcon(R.drawable.core_notifications_ic_aiforum_notification)
                    .setAutoCancel(true)
            }
            val notificationManager = NotificationManagerCompat.from(this)
            wordsNotification.forEachIndexed { index, notification ->
                notificationManager.notify(
                    truncatedWords[index].wordId.hashCode(),
                    notification
                )
            }
            notificationManager.notify(WORDS_NOTIFICATION_SUMMARY_ID, summaryNotification)
        }
    }
    private fun wordsNotificationStyle( // 折叠式style，android推荐
        words: List<Word>,
        title: String,
    ): InboxStyle = words
        .fold(InboxStyle()) { inboxStyle, word -> inboxStyle.addLine(word.wordName) }
        .setBigContentTitle(title)
        .setSummaryText(title)

}
private fun Context.createWordsNotification(
    block : NotificationCompat.Builder.() -> Unit,
) : Notification {
    ensureNotificationChannelExists()
    return NotificationCompat.Builder(
        this,
        WORDS_NOTIFICATION_CHANNEL_ID
    )
        .setPriority(NotificationCompat.PRIORITY_DEFAULT)
        .apply(block)
        .build()
}
private fun Context.wordsPendingIntent(
    word: Word,
) : PendingIntent? =
    PendingIntent.getActivity(
        this,
        WORDS_NOTIFICATION_REQUEST_CODE,
        Intent().apply {
            action = Intent.ACTION_VIEW //告诉系统这是一个“查看”操作。
            data = word.wordsDeepLinkUri() //指定要查看的具体内容地址
            component = ComponentName(
                packageName,
                TARGET_ACTIVITY_NAME,
            )//显式指定处理这个 Intent 的组件（Activity）
        },
        //FLAG_IMMUTABLE 从 Android 12 开始，除非必须，否则所有 PendingIntent 都强制要求加上这个标志。它表示这个 Intent 一旦创建，接收方（系统）就不能修改它的内容。
        PendingIntent.FLAG_IMMUTABLE or PendingIntent.FLAG_UPDATE_CURRENT
    )
private fun Context.ensureNotificationChannelExists(

) {
    if (Build.VERSION.SDK_INT < Build.VERSION_CODES.O) return
    val channel = NotificationChannel(
        WORDS_NOTIFICATION_CHANNEL_ID,
            getString(R.string.core_notifications_words_notification_channel_name),
        NotificationManager.IMPORTANCE_DEFAULT
    ).apply {
        description = getString(R.string.core_notifications_words_notification_channel_description)
    }
    NotificationManagerCompat.from(this).createNotificationChannel(channel)

}
private fun Word.wordsDeepLinkUri() = "$DEEP_LINK_BASE_PATH/$wordId".toUri()