package ai.ljp.analytics

import android.util.Log

class LogAnalyticsHelper : AnalyticsHelper {
    override fun logEvent(event: AnalyticsEvent) {
        Log.e("LogAnalyticsHelper",event.toString())
    }
}