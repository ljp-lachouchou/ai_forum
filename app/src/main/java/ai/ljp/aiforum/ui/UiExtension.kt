package ai.ljp.aiforum.ui

import ai.ljp.analytics.AnalyticsEvent
import ai.ljp.analytics.AnalyticsHelper
import android.content.res.Configuration
import androidx.activity.ComponentActivity
import androidx.compose.runtime.compositionLocalOf
import androidx.compose.runtime.staticCompositionLocalOf
import androidx.core.util.Consumer
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.flow.conflate
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.datetime.TimeZone

//uiMode表示一个聚合
//Configuration.UI_MODE_NIGHT_MASK表示夜间模式位掩码
//(uiMode and Configuration.UI_MODE_NIGHT_MASK) 只保留关于夜间模式的内容
val Configuration.isSystemInDarkTheme
    get() = (uiMode and Configuration.UI_MODE_NIGHT_MASK) == Configuration.UI_MODE_NIGHT_YES

fun ComponentActivity.isSystemDarkTheme() = callbackFlow{// 配置更新了就重新发送一条消息
    channel.trySend(resources.configuration.isSystemInDarkTheme)
    val listener = Consumer<Configuration> {
        channel.trySend(it.isSystemInDarkTheme)
    }
    addOnConfigurationChangedListener(listener)
    awaitClose { removeOnConfigurationChangedListener(listener) }
}
    .distinctUntilChanged()
    .conflate()

val LocalAnalyticsHelper = staticCompositionLocalOf<AnalyticsHelper> {
    // Provide a default AnalyticsHelper which does nothing. This is so that tests and previews
    // do not have to provide one. For real app builds provide a different implementation.
    object : AnalyticsHelper {
        override fun logEvent(event: AnalyticsEvent) = Unit
    }
}
val LocalTimezone = compositionLocalOf { TimeZone.currentSystemDefault() }