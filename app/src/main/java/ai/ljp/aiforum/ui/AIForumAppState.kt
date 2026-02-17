package ai.ljp.aiforum.ui

import ai.ljp.aiforum.navigation.TOP_LEVEL_NAV_ITEMS
import ai.ljp.data.repository.UserDataRepository
import ai.ljp.data.util.NetworkMonitor
import ai.ljp.data.util.TimezoneMonitor
import ai.ljp.navigation.NavigationState
import ai.ljp.navigation.rememberNavigationState
import android.util.Log
import androidx.compose.runtime.Composable
import androidx.compose.runtime.Stable
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.lifecycle.viewModelScope
import feature.ljp.home.api.HomeKey
import feature.ljp.login.api.LoginKey
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.datetime.TimeZone

@Composable
fun rememberAIForumAppState(
    networkMonitor: NetworkMonitor,
    timeZoneMonitor: TimezoneMonitor,
    userDataRepository: UserDataRepository,
    coroutineScope: CoroutineScope = rememberCoroutineScope(),
) : AIForumAppState {
    val navigationState = rememberNavigationState(
        startKey = HomeKey,
        topKeys = TOP_LEVEL_NAV_ITEMS.keys
    )
    return remember(
        networkMonitor,
        timeZoneMonitor,
        coroutineScope
    ) {
        AIForumAppState(
            navigationState = navigationState,
            timezoneMonitor = timeZoneMonitor,
            networkMonitor = networkMonitor,
            coroutineScope = coroutineScope,
            userDataRepository = userDataRepository,
        )
    }
}
@Stable
class AIForumAppState(
    val navigationState: NavigationState,
    networkMonitor: NetworkMonitor,
    timezoneMonitor: TimezoneMonitor,
    userDataRepository: UserDataRepository,
    coroutineScope: CoroutineScope,
) {
    val isLogin : StateFlow<Boolean> = userDataRepository.userData
        .map {
            Log.e("sdasdasdasd","${it.currentUserId}")
            Log.e("sdasdasdasd","${it.authToken}")
            it.currentUserId?.isNotBlank() == true && it.authToken?.isNotBlank() == true
        }
        .stateIn(
            scope = coroutineScope,
            initialValue = false,
            started = SharingStarted.WhileSubscribed(5_000)
        )
    val isOffline = networkMonitor.isOnline
        .map(Boolean::not)
        .stateIn(
            scope = coroutineScope,
            started = SharingStarted.WhileSubscribed(5_000),
            initialValue = false
        )
    val currentTimezone = timezoneMonitor.currentTimezone
        .stateIn(
            coroutineScope,
            SharingStarted.WhileSubscribed(5_000),
            TimeZone.currentSystemDefault(),
        )
}