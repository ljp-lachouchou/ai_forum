package ai.ljp.data.repository

import ai.ljp.analytics.AnalyticsHelper
import ai.ljp.datastore.AIForumPreferencesDatastore
import com.ljp.model.DarkThemeConfig
import com.ljp.model.MoodThemeConfig
import com.ljp.model.ThemeBrand
import com.ljp.model.UserData
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class OfflineFirstUserDataRepository @Inject constructor(
    private val userDataPreferencesDatastore: AIForumPreferencesDatastore,
    private val asyncHelper: AnalyticsHelper
) : UserDataRepository {
    override val userDat: Flow<UserData> = userDataPreferencesDatastore.userData

    override suspend fun setAuthToken(authToken: String) {
        userDataPreferencesDatastore.setAuthToken(authToken)
        asyncHelper.logAuthToken(authToken)
    }


    override suspend fun setMoodThemeConfig(moodThemeConfig: MoodThemeConfig) =
        userDataPreferencesDatastore.setMoodThemeConfig(moodThemeConfig)

    override suspend fun setCurrentUserId(currentUserId: String) {
        userDataPreferencesDatastore.setCurrentUserId(currentUserId)
        asyncHelper.logCurrentId(currentUserId)
    }

    override suspend fun setThemeBrand(themeBrand: ThemeBrand) =
        userDataPreferencesDatastore.setThemeBrand(themeBrand)

    override suspend fun setDynamicColorPreference(useDynamicColor: Boolean) =
        userDataPreferencesDatastore.setDynamicColorPreference(useDynamicColor)

    override suspend fun setDarkThemeConfig(darkThemeConfig: DarkThemeConfig) =
        userDataPreferencesDatastore.setDarkThemeConfig(darkThemeConfig)

    override suspend fun setShouldHideOnboarding(shouldHideOnboarding: Boolean) {
        userDataPreferencesDatastore.setShouldHideOnboarding(shouldHideOnboarding)
        asyncHelper.logOnboardingStateChanged(shouldHideOnboarding)
    }

}