package ai.ljp.analytics

import com.google.firebase.analytics.FirebaseAnalytics
import com.google.firebase.analytics.logEvent
import javax.inject.Inject
import javax.inject.Provider


class FirebaseAnalyticsHelper @Inject constructor(
    private val firebaseAnalytics: Provider<FirebaseAnalytics>
) : AnalyticsHelper {
    override fun logEvent(event: AnalyticsEvent) {
        firebaseAnalytics.get().logEvent(event.type) {
            for (extra in event.extras) {
                // 根据 Firebase 最大长度值截断参数键和值。
                param(
                    key = extra.key.take(40),
                    value = extra.value.take(100),
                )
            }
        }
    }
}