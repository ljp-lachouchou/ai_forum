package ai.ljp.analytics

import com.google.firebase.analytics.FirebaseAnalytics
import com.google.firebase.analytics.logEvent
import javax.inject.Inject


class FirebaseAnalyticsHelper @Inject constructor(
    private val firebaseAnalytics: dagger.Lazy<FirebaseAnalytics>
) : AnalyticsHelper {
    override fun logEvent(event: AnalyticsEvent) {
        try {
            // 尝试获取实例并记录日志
            firebaseAnalytics.get().logEvent(event.type) {
                for (extra in event.extras) {
                    param(
                        key = extra.key.take(40),
                        value = extra.value.take(100),
                    )
                }
            }
        } catch (e: IllegalStateException) {
            // 捕获 "Default FirebaseApp is not initialized" 异常
            // 这里的策略是：如果 Firebase 还没准备好，就放弃记录这条日志
            // 这样不会导致 Worker 崩溃，同步任务可以继续进行
            android.util.Log.w("Analytics", "Firebase not ready yet, skipping log: ${event.type}")
        } catch (e: Exception) {
            // 捕获其他可能的异常
            android.util.Log.e("Analytics", "Failed to log event", e)
        }
    }
}