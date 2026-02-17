package ai.ljp.data.util

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.os.Build
import androidx.tracing.trace
import com.ljp.common.network.AIForumDispatchers
import com.ljp.common.network.Dispatcher
import com.ljp.common.network.di.ApplicationScope
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.shareIn
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toKotlinTimeZone
import java.time.ZoneId
import javax.inject.Inject
import javax.inject.Singleton

interface TimezoneMonitor {
    val currentTimezone : Flow<TimeZone>
}

@Singleton
class BroadcastTimezoneMonitor @Inject constructor(
    @ApplicationScope private val appScope: CoroutineScope,
    @ApplicationContext private val context: Context,
    @Dispatcher(AIForumDispatchers.IO) private val ioDispatcher: CoroutineDispatcher,
) : TimezoneMonitor {
    override val currentTimezone: SharedFlow<TimeZone>
        get() = callbackFlow {
            trySend(TimeZone.currentSystemDefault())
            val receiver = object : BroadcastReceiver() {
                override fun onReceive(context: Context, intent: Intent) {
                    if (intent.action != Intent.ACTION_TIMEZONE_CHANGED) return
                    val zoneId = if (Build.VERSION.SDK_INT <= Build.VERSION_CODES.R) {
                        null
                    }else {
                        intent.getStringExtra(Intent.EXTRA_TIMEZONE)?.let {timeZoneId->
                            val zoneId = ZoneId.of(timeZoneId,ZoneId.SHORT_IDS)
                            zoneId.toKotlinTimeZone()
                        }
                    }
                    trySend(zoneId ?: TimeZone.currentSystemDefault())
                }
            }
            trace("TimeZoneBroadcastReceiver.register") {
                context.registerReceiver(receiver, IntentFilter(Intent.ACTION_TIMEZONE_CHANGED))
            }
            //因为注册接收器需要花费一定事件，这时候如果订阅者改变了timezone，这时候能够发出去
            trySend(TimeZone.currentSystemDefault())

            awaitClose {
                context.unregisterReceiver(receiver)
            }
        }
            .distinctUntilChanged()
            .shareIn(
                scope = appScope,
                started = SharingStarted.WhileSubscribed(5_000),
                replay = 1
            )

}