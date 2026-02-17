package ai.ljp.aiforum

import ai.ljp.data.repository.UserDataRepository
import ai.ljp.designsystem.theme.Mood
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ljp.model.DarkThemeConfig
import com.ljp.model.MoodThemeConfig
import com.ljp.model.ThemeBrand
import com.ljp.model.UserData
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import javax.inject.Inject

@HiltViewModel
class MainActivityViewModel @Inject constructor(
    userDataRepository: UserDataRepository
) : ViewModel() {
    val uiState : StateFlow<MainActivityUiState> =
        userDataRepository.userData
            .map(MainActivityUiState::Success)
            .stateIn(
                scope = viewModelScope,
                initialValue = MainActivityUiState.Loading,
                started = SharingStarted.WhileSubscribed(5_000)
            )


}
sealed interface MainActivityUiState {
    data object Loading : MainActivityUiState

    data class Success(val userData: UserData) : MainActivityUiState {
        override val shouldDisableDynamicTheming: Boolean
            get() = !userData.useDynamicColor
        override val shouldUseAndroidTheme: Boolean = when(userData.themeBrand) {
            ThemeBrand.DEFAULT -> false
            ThemeBrand.ANDROID -> true
        }

        override fun shouldUseDarkTheme(isSystemDarkTheme: Boolean): Boolean =
            when (userData.darkThemeConfig) {
                DarkThemeConfig.FOLLOW_SYSTEM -> isSystemDarkTheme
                DarkThemeConfig.LIGHT -> false
                DarkThemeConfig.DARK -> true
            }

        override val currentMood: Mood = when(userData.moodThemeConfig) {
            MoodThemeConfig.Sad -> Mood.Sad
            MoodThemeConfig.Happy -> Mood.Happy
            MoodThemeConfig.Normal -> Mood.Normal
            MoodThemeConfig.Anxiety -> Mood.Anxiety
        }
    }
    fun shouldKeepSplashScreen() = this is Loading

    val shouldDisableDynamicTheming: Boolean get() = true
    val shouldUseAndroidTheme: Boolean get() = false
    val currentMood : Mood get() = Mood.Normal
    fun shouldUseDarkTheme(isSystemDarkTheme: Boolean) = isSystemDarkTheme
}