package com.ljp.model

enum class MoodThemeConfig {
    Normal,
    Sad,
    Happy,
    Anxiety;
    companion object {
        fun fromMood(moodThemeConfig: MoodThemeConfig) = when(moodThemeConfig) {
            MoodThemeConfig.Anxiety -> "焦虑"
            MoodThemeConfig.Sad -> "伤心"
            Happy -> "开心"
            Normal -> "平常"
        }
    }
}