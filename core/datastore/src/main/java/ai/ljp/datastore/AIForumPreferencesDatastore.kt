package ai.ljp.datastore

import android.util.Log
import androidx.datastore.core.DataStore
import com.ljp.common.token.TokenProvider
import com.ljp.model.DarkThemeConfig
import com.ljp.model.MoodThemeConfig
import com.ljp.model.ThemeBrand
import com.ljp.model.UserData
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.flow.map
import okio.IOException
import javax.inject.Inject

class AIForumPreferencesDatastore @Inject constructor(
    private val userPreferences : DataStore<UserPreferences>
) : TokenProvider{
    val userData = userPreferences.data
        .map {
            UserData(
                darkThemeConfig = when(it.darkThemeConfig) {
                    null,
                    DarkThemeConfigProto.DARK_THEME_CONFIG_UNSPECIFIED,
                    DarkThemeConfigProto.UNRECOGNIZED,
                    DarkThemeConfigProto.DARK_THEME_CONFIG_FOLLOW_SYSTEM ->
                        DarkThemeConfig.FOLLOW_SYSTEM
                    DarkThemeConfigProto.DARK_THEME_CONFIG_LIGHT ->
                        DarkThemeConfig.LIGHT
                    DarkThemeConfigProto.DARK_THEME_CONFIG_DARK ->
                        DarkThemeConfig.DARK
                },
                themeBrand = when(it.themeBrand) {
                    null,
                    ThemeBrandProto.THEME_BRAND_UNSPECIFIED,
                    ThemeBrandProto.UNRECOGNIZED,
                    ThemeBrandProto.THEME_BRAND_DEFAULT ->
                        ThemeBrand.DEFAULT
                    ThemeBrandProto.THEME_BRAND_ANDROID ->
                        ThemeBrand.ANDROID
                },
                useDynamicColor = it.useDynamicColor,
                shouldHideOnboarding = it.shouldHideOnboarding, // 是否展示初次设置时的界面
                currentUserId = it.currentUserId,
                authToken = it.authToken,
                lastSyncVersion = it.lastSyncVersion,
                moodThemeConfig = when (it.moodThemeConfig) {
                    null,
                    MoodThemeConfigProto.MOOD_THEME_CONFIG_NORMAL,
                    MoodThemeConfigProto.UNRECOGNIZED ->
                        MoodThemeConfig.Normal
                    MoodThemeConfigProto.MOOD_THEME_CONFIG_SAD ->
                        MoodThemeConfig.Sad
                    MoodThemeConfigProto.MOOD_THEME_CONFIG_HAPPY ->
                        MoodThemeConfig.Happy
                    MoodThemeConfigProto.MOOD_THEME_CONFIG_ANXIETY ->
                        MoodThemeConfig.Anxiety
                }
            )
        }
    suspend fun setMoodThemeConfig(moodThemeConfig: MoodThemeConfig) {
        userPreferences.updateData {
            it.copy {
                this.moodThemeConfig = when(moodThemeConfig) {
                    MoodThemeConfig.Anxiety -> MoodThemeConfigProto.MOOD_THEME_CONFIG_ANXIETY
                    MoodThemeConfig.Happy -> MoodThemeConfigProto.MOOD_THEME_CONFIG_HAPPY
                    MoodThemeConfig.Sad -> MoodThemeConfigProto.MOOD_THEME_CONFIG_SAD
                    MoodThemeConfig.Normal -> MoodThemeConfigProto.MOOD_THEME_CONFIG_NORMAL
                }
            }
        }
    }
    suspend fun setCurrentUserId(currentUserId : String) {
        userPreferences.updateData {
            it.copy {
                this.currentUserId = currentUserId
            }
        }
    }
    suspend fun setIdAndToken(token : String,userId : String) {
        userPreferences.updateData {
            it.copy {
                this.currentUserId = userId
                this.authToken = token
            }
        }
    }
    suspend fun setThemeBrand(themeBrand: ThemeBrand) {
        userPreferences.updateData {
            it.copy {
                this.themeBrand = when(themeBrand) {
                    ThemeBrand.ANDROID -> ThemeBrandProto.THEME_BRAND_ANDROID
                    ThemeBrand.DEFAULT -> ThemeBrandProto.THEME_BRAND_DEFAULT
                }
            }
        }
    }
    suspend fun setDynamicColorPreference(useDynamicColor : Boolean) {
        userPreferences.updateData {
            it.copy {
                this.useDynamicColor = useDynamicColor
            }
        }
    }
    suspend fun setDarkThemeConfig(darkThemeConfig: DarkThemeConfig) {
        userPreferences.updateData {
            it.copy {
                this.darkThemeConfig = when(darkThemeConfig) {
                    DarkThemeConfig.FOLLOW_SYSTEM -> DarkThemeConfigProto.DARK_THEME_CONFIG_FOLLOW_SYSTEM
                    DarkThemeConfig.DARK -> DarkThemeConfigProto.DARK_THEME_CONFIG_DARK
                    DarkThemeConfig.LIGHT -> DarkThemeConfigProto.DARK_THEME_CONFIG_LIGHT
                }
            }
        }
    }
    suspend fun getChangeVersion() = userPreferences.data.map {
        ChangeVersion(
            syncVersion = it.lastSyncVersion
        )
    }.firstOrNull() ?: ChangeVersion()

    suspend fun updateChangeVersion(update : ChangeVersion.() -> ChangeVersion) {
        try { // 后台同步需要保证version的正确性
            userPreferences.updateData { currentUserPreferences->
                val updatedChangeVersion = update(
                    ChangeVersion(
                        currentUserPreferences.lastSyncVersion
                    )
                )
                currentUserPreferences.copy {
                    this.lastSyncVersion = updatedChangeVersion.syncVersion
                }
            }
        } catch (exception : IOException) {
            Log.e("AIForumPreferences", "Failed to update user preferences", exception)
        }
    }

    suspend fun setShouldHideOnboarding(shouldHideOnboarding : Boolean) {
        userPreferences.updateData {
            it.copy {
                this.shouldHideOnboarding = shouldHideOnboarding
            }
        }
    }

    override suspend fun getToken(): String? =
        userPreferences.data.map {
            it.authToken.takeIf {token-> token.isNotBlank() }
        }.firstOrNull()

    override suspend fun setAuthToken(token: String?) {
        userPreferences.updateData {
            it.copy {
                this.authToken = token ?: ""
            }
        }
    }

    override suspend fun clearToken() {
        userPreferences.updateData {
            it.copy {
                this.authToken = ""
                this.currentUserId = ""
            }
        }
    }


}