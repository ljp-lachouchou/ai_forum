package ai.ljp.designsystem.theme

import androidx.compose.runtime.staticCompositionLocalOf

enum class Mood {
    Normal,
    Sad,
    Happy,
    Anxiety
}
val LocalMood = staticCompositionLocalOf { Mood.Normal }