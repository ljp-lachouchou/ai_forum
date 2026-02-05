package ai.ljp.sync.worker

import ai.ljp.analytics.AnalyticsEvent
import ai.ljp.analytics.AnalyticsHelper

internal fun AnalyticsHelper.logSyncStarted() =
    logEvent(
        AnalyticsEvent(type = "network_sync_started"),
    )

internal fun AnalyticsHelper.logSyncFinished(syncSuccessfully : Boolean) {
    val eventType =if (syncSuccessfully)"network_sync_successful" else "network_sync_failed"
    logEvent(
        AnalyticsEvent(type = eventType)
    )
}