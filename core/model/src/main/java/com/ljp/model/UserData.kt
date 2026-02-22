package com.ljp.model

/**
 * 本地离线支持的设置
 */
data class UserData(
    val darkThemeConfig: DarkThemeConfig,
    val themeBrand: ThemeBrand,
    val useDynamicColor: Boolean,
    val shouldHideOnboarding: Boolean, // 打开时，是否需要选择一些东西，作为画像进行文章推荐
    val currentUserId: String?,
    val authToken: String?,
    val lastSyncVersion: Long = 0L,
    val moodThemeConfig : MoodThemeConfig

)
data class Settings(
    val darkThemeConfig: DarkThemeConfig,
    val themeBrand: ThemeBrand,
    val useDynamicColor: Boolean,
    val debugToken: Double = Math.random()
) {
    constructor() : this(
        darkThemeConfig = DarkThemeConfig.FOLLOW_SYSTEM,
        themeBrand = ThemeBrand.DEFAULT,
        useDynamicColor = false
    )
}
fun UserData.asSetting() =
    Settings(
        darkThemeConfig = darkThemeConfig,
        themeBrand = themeBrand,
        useDynamicColor = useDynamicColor
    )