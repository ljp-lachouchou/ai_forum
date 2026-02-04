package ai.ljp.data.repository

import com.ljp.model.DarkThemeConfig
import com.ljp.model.MoodThemeConfig
import com.ljp.model.ThemeBrand
import com.ljp.model.UserData
import kotlinx.coroutines.flow.Flow

interface UserDataRepository {
    val userDat : Flow<UserData>
    suspend fun setAuthToken (authToken : String)
    suspend fun setMoodThemeConfig(moodThemeConfig: MoodThemeConfig)
    suspend fun setCurrentUserId(currentUserId : String)
    suspend fun setThemeBrand(themeBrand: ThemeBrand)
    suspend fun setDynamicColorPreference(useDynamicColor : Boolean)
    suspend fun setDarkThemeConfig(darkThemeConfig: DarkThemeConfig)

    suspend fun setShouldHideOnboarding(shouldHideOnboarding : Boolean)
}