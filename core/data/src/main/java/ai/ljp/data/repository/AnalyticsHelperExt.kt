package ai.ljp.data.repository

import ai.ljp.analytics.AnalyticsEvent
import ai.ljp.analytics.AnalyticsHelper


internal fun AnalyticsHelper.logCurrentId(currentId : String) {
    logEvent(
        AnalyticsEvent(
            type = "current_user_id_saved",
            extras = listOf(
                AnalyticsEvent.Param("current_user_id",currentId)
            )
        )
    )
}

internal fun AnalyticsHelper.logAuthToken(authToken : String) {
    logEvent(
        AnalyticsEvent(
            type = "current_auth_token_saved",
            extras = listOf(
                AnalyticsEvent.Param("current_auth_token",authToken)
            )
        )
    )
}

internal fun AnalyticsHelper.logOnboardingStateChanged(shouldHideOnboarding: Boolean) {
    val eventType = if (shouldHideOnboarding) "onboarding_complete" else "onboarding_reset"
    logEvent(
        AnalyticsEvent(type = eventType),
    )
}