package ai.ljp.aiforum

import ai.ljp.aiforum.ui.AiForumApp
import ai.ljp.aiforum.ui.LocalAnalyticsHelper
import ai.ljp.aiforum.ui.LocalTimezone
import ai.ljp.aiforum.ui.isSystemDarkTheme
import ai.ljp.aiforum.ui.isSystemInDarkTheme
import ai.ljp.aiforum.ui.rememberAIForumAppState
import ai.ljp.analytics.AnalyticsHelper
import ai.ljp.data.repository.UserDataRepository
import ai.ljp.data.util.NetworkMonitor
import ai.ljp.data.util.TimezoneMonitor
import ai.ljp.designsystem.theme.AIForumTheme
import ai.ljp.designsystem.theme.Mood
import android.graphics.Color
import android.os.Bundle
import android.util.Log

import androidx.activity.ComponentActivity
import androidx.activity.SystemBarStyle
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.metrics.performance.JankStats
import dagger.Lazy
import dagger.hilt.android.AndroidEntryPoint
import feature.ljp.login.impl.LoginUiState
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import kotlinx.datetime.TimeZone


import javax.inject.Inject
import kotlin.getValue
@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    @Inject
    lateinit var lazyStats : Lazy<JankStats>

    @Inject
    lateinit var networkMonitor: NetworkMonitor

    @Inject
    lateinit var timeZoneMonitor: TimezoneMonitor

    @Inject
    lateinit var analyticsHelper: AnalyticsHelper

    @Inject
    lateinit var userDataRepository: UserDataRepository

    private val mainActivityViewModel : MainActivityViewModel by viewModels()
    override fun onCreate(savedInstanceState: Bundle?) {
        val splashScreen = installSplashScreen()
        super.onCreate(savedInstanceState)
        var themeSettings by mutableStateOf(
            ThemeSettings(
                darkTheme = resources.configuration.isSystemInDarkTheme,
                androidTheme = MainActivityUiState.Loading.shouldUseAndroidTheme,
                disableDynamicTheming = MainActivityUiState.Loading.shouldDisableDynamicTheming,
                mood = MainActivityUiState.Loading.currentMood
            )
        )
        lifecycleScope.launch {
            lifecycle.repeatOnLifecycle(Lifecycle.State.STARTED) {
                combine(
                    isSystemDarkTheme(),
                    mainActivityViewModel.uiState
                ) {systemDark, uiState->
                    ThemeSettings(
                        darkTheme = uiState.shouldUseDarkTheme(systemDark),
                        androidTheme = uiState.shouldUseAndroidTheme,
                        disableDynamicTheming = uiState.shouldDisableDynamicTheming,
                        mood = uiState.currentMood
                    )
                }
                    .onEach {
                        Log.e("themeSettings = ",themeSettings.toString())
                        themeSettings = it
                    }
                    .map { it.darkTheme }
                    .distinctUntilChanged()
                    .collect { darkTheme->
                        enableEdgeToEdge(
                            statusBarStyle = SystemBarStyle.auto(
                                lightScrim = Color.TRANSPARENT,
                                darkScrim = Color.TRANSPARENT,
                            ) { darkTheme },
                            navigationBarStyle = SystemBarStyle.auto(
                                lightScrim = lightScrim,
                                darkScrim = darkScrim,
                            ) { darkTheme },
                        )
                    }
            }
        }
        splashScreen.setKeepOnScreenCondition { mainActivityViewModel.uiState.value.shouldKeepSplashScreen() }
        setContent {
            val appState = rememberAIForumAppState(
                networkMonitor = networkMonitor,
                timeZoneMonitor = timeZoneMonitor,
                userDataRepository = userDataRepository,
            )
            val currentTimezone by appState.currentTimezone.collectAsStateWithLifecycle()
            CompositionLocalProvider(
                LocalAnalyticsHelper provides analyticsHelper,
                LocalTimezone provides currentTimezone,
            ) {
                AIForumTheme(
                    mood = themeSettings.mood,
                    darkTheme = themeSettings.darkTheme,
                    androidTheme = themeSettings.androidTheme,
                    disableDynamicTheming = themeSettings.disableDynamicTheming,
                ) {
                    AiForumApp(appState)
                }
            }
        }

    }


}
/**
 * The default light scrim, as defined by androidx and the platform:
 * https://cs.android.com/androidx/platform/frameworks/support/+/androidx-main:activity/activity/src/main/java/androidx/activity/EdgeToEdge.kt;l=35-38;drc=27e7d52e8604a080133e8b842db10c89b4482598
 */
private val lightScrim = Color.argb(0xe6, 0xFF, 0xFF, 0xFF)

/**
 * The default dark scrim, as defined by androidx and the platform:
 * https://cs.android.com/androidx/platform/frameworks/support/+/androidx-main:activity/activity/src/main/java/androidx/activity/EdgeToEdge.kt;l=40-44;drc=27e7d52e8604a080133e8b842db10c89b4482598
 */
private val darkScrim = Color.argb(0x80, 0x1b, 0x1b, 0x1b)
data class ThemeSettings(
    val darkTheme: Boolean,
    val androidTheme: Boolean,
    val disableDynamicTheming: Boolean,
    val mood: Mood
)