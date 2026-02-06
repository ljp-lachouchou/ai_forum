package ai.ljp.sync.initializer

import ai.ljp.sync.R
import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.net.NetworkRequest
import android.os.Build
import androidx.core.app.NotificationCompat
import androidx.work.Constraints
import androidx.work.ForegroundInfo
import androidx.work.NetworkType

const val SYNC_TOPIC = "sync"
private const val SYNC_NOTIFICATION_ID = 0
internal const val SYNC_WORK_NAME = "SyncWorkName"
private const val SYNC_NOTIFICATION_CHANNEL_ID = "SyncNotificationChannel"
val SyncConstraints
    get() = Constraints.Builder()
        .setRequiredNetworkType(NetworkType.CONNECTED)
        .build()

fun Context.syncForegroundInfo() = ForegroundInfo(
    SYNC_NOTIFICATION_ID,
    syncWorkNotification()
)
private fun Context.syncWorkNotification() : Notification {
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
        val channel = NotificationChannel(
            SYNC_NOTIFICATION_CHANNEL_ID,
            getString(R.string.core_sync_sync_channel_name),
            NotificationManager.IMPORTANCE_DEFAULT
        ).apply {
            description =getString(R.string.sync_work_notification_channel_description)
        }
        val notificationManager =
            getSystemService(Context.NOTIFICATION_SERVICE) as? NotificationManager
        notificationManager?.createNotificationChannel(channel)
    }
    return NotificationCompat.Builder(
        this,
        SYNC_NOTIFICATION_CHANNEL_ID
    )
        .setContentTitle(getString(R.string.sync_work_notification_title))
        .setSmallIcon(ai.ljp.notification.R.drawable.core_notifications_ic_aiforum_notification)
        .setPriority(NotificationCompat.PRIORITY_DEFAULT)
        .build()

}