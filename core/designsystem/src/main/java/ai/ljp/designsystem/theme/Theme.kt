package ai.ljp.designsystem.theme

import android.os.Build
import androidx.annotation.ChecksSdkIntAtLeast
import androidx.annotation.VisibleForTesting
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.ColorScheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.dynamicDarkColorScheme
import androidx.compose.material3.dynamicLightColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.material3.surfaceColorAtElevation
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp

// --- Normal (平静) ---
val LightDefaultNormalColorTheme = lightColorScheme(
    primary = Blue100,//发帖按钮 (FAB)、选中的导航图标、主要按钮背景。
    onPrimary = PureWhite,//发帖按钮上的“+”号或文字颜色（通常为白色或黑色）。
    primaryContainer = Blue10,//列表条目的背景高亮、输入框的填充色、AI 总结卡片的底色。
    onPrimaryContainer = Blue100,
    background = Slate50,//整个页面的底色。
    surface = PureWhite,//论坛帖子的卡片 (Card)、弹窗 (Dialog)、底部菜单栏 (Navigation Bar)。
    surfaceVariant = Slate50,
    onSurface = PureBlack,//帖子正文文字、设置菜单的标题。
    onSurfaceVariant = Black40,
    outline = Black5
)

val LightAndroidNormalColorTheme = lightColorScheme(
    primary = Blue100,
    onPrimary = PureWhite,
    primaryContainer = Blue20, // Blue20
    onPrimaryContainer = Blue100,
    background = Blue10,
    surface = Blue10,
    surfaceVariant = Blue10,
    onSurface = PureBlack,
    outline = Black5
)

// --- Happy (开心) ---
val LightDefaultHappyColorTheme = lightColorScheme(
    primary = Amber100,
    onPrimary = PureBlack,
    primaryContainer = Amber10,
    onPrimaryContainer = Amber100,
    background = Amber50,
    surface = PureWhite,
    surfaceVariant = Slate50,
    onSurface = PureBlack,
    onSurfaceVariant = Black40,
    outline = Black5
)

val LightAndroidHappyColorTheme = lightColorScheme(
    primary = Amber100,
    onPrimary = PureBlack,
    primaryContainer = Amber20, // Amber20
    onPrimaryContainer = Amber100,
    background = Amber10,
    surface = Amber10,
    surfaceVariant = Amber10,
    onSurface = PureBlack,
    outline = Black5
)

// --- Sad (悲伤) ---
val LightDefaultSadColorTheme = lightColorScheme(
    primary = Slate600,
    onPrimary = PureWhite,
    primaryContainer = Slate20,
    onPrimaryContainer = Slate600,
    background = Slate100,
    surface = Slate100,
    surfaceVariant = Slate100,
    onSurface = PureBlack,
    onSurfaceVariant = Black40,
    outline = Black5
)

val LightAndroidSadColorTheme = lightColorScheme(
    primary = Slate600,
    onPrimary = PureWhite,
    primaryContainer = Slate30, // Slate30
    onPrimaryContainer = Slate600,
    background = Slate20,
    surface = Slate20,
    surfaceVariant = Slate20,
    onSurface = PureBlack,
    outline = Black5
)

// --- Anxiety (焦虑) ---
val LightDefaultAnxietyColorTheme = lightColorScheme(
    primary = Purple100,
    onPrimary = PureWhite,
    primaryContainer = Purple10,
    onPrimaryContainer = Purple100,
    background = Purple50,
    surface = PureWhite,
    surfaceVariant = Slate50,
    onSurface = PureBlack,
    onSurfaceVariant = Black40,
    outline = Black5
)

val LightAndroidAnxietyColorTheme = lightColorScheme(
    primary = Purple100,
    onPrimary = PureWhite,
    primaryContainer = Purple20, // Purple20
    onPrimaryContainer = Purple100,
    background = Purple10,
    surface = Purple10,
    surfaceVariant = Purple10,
    onSurface = PureBlack,
    outline = Black5
)

// --- Normal (平静) ---
val DarkDefaultNormalColorTheme = darkColorScheme(
    primary = Blue100,
    onPrimary = PureBlack,
    primaryContainer = Blue20,
    onPrimaryContainer = Blue100,
    background = DarkBg,
    surface = DarkSurface,
    onSurface = PureWhite,
    outline = White10
)

val DarkAndroidNormalColorTheme = darkColorScheme(
    primary = Blue100,
    onPrimary = PureBlack,
    primaryContainer = Blue30,
    background = DarkBgNormal, // Tonal Dark
    surface = DarkSurfaceNormal,
    onSurface = PureWhite
)

// --- Happy (开心) ---
val DarkDefaultHappyColorTheme = darkColorScheme(
    primary = Amber100,
    onPrimary = PureBlack,
    primaryContainer = Amber20,
    onPrimaryContainer = Amber100,
    background = DarkBg,
    surface = DarkSurface,
    onSurface = PureWhite
)

val DarkAndroidHappyColorTheme = darkColorScheme(
    primary = Amber100,
    onPrimary = PureBlack,
    primaryContainer = Amber30,
    background = DarkBgHappy, // Tonal Warm Dark
    surface = DarkSurfaceHappy,
    onSurface = PureWhite
)

// --- Sad (悲伤) ---
val DarkDefaultSadColorTheme = darkColorScheme(
    primary = Slate600,
    onPrimary = PureWhite,
    primaryContainer = Slate30,
    onPrimaryContainer = Slate600,
    background = DarkBg,
    surface = DarkSurface,
    onSurface = PureWhite
)

val DarkAndroidSadColorTheme = darkColorScheme(
    primary = Slate600,
    onPrimary = PureWhite,
    primaryContainer = Slate40,
    background = DarkBgSad, // Tonal Cool Dark
    surface = DarkSurfaceNormal,
    onSurface = PureWhite
)

// --- Anxiety (焦虑) ---
val DarkDefaultAnxietyColorTheme = darkColorScheme(
    primary = Purple100,
    onPrimary = PureBlack,
    primaryContainer = Purple20,
    onPrimaryContainer = Purple100,
    background = DarkBg,
    surface = DarkSurface,
    onSurface = PureWhite
)

val DarkAndroidAnxietyColorTheme = darkColorScheme(
    primary = Purple100,
    onPrimary = PureBlack,
    primaryContainer = Purple30,
    background = DarkBgAnxiety, // Tonal Purple Dark
    surface = DarkSurfaceAnxiety,
    onSurface = PureWhite
)

val DarkAndroidNormalGradientColor = GradientColors(bottom = DarkBgNormal, top = Blue20)
val LightAndroidNormalGradientColor = GradientColors(bottom = Blue10, top = Blue20)

val DarkAndroidSadGradientColor = GradientColors(bottom = DarkBgSad, top = Slate30)
val LightAndroidSadGradientColor = GradientColors(bottom = Slate20, top = Slate30)

val DarkAndroidHappyGradientColor = GradientColors(bottom = DarkBgHappy, top = Amber20)
val LightAndroidHappyGradientColor = GradientColors(bottom = Amber10, top = Amber20)

val DarkAndroidAnxietyGradientColor = GradientColors(bottom = DarkBgAnxiety, top = Purple20)
val LightAndroidAnxietyGradientColor = GradientColors(bottom = Purple10, top = Purple20)

val LightAndroidNormalBackground = BackgroundTheme(color = Blue10, tonalElevation = 2.dp)
val DarkAndroidNormalBackground = BackgroundTheme(color = DarkBgNormal, tonalElevation = 2.dp)

val LightAndroidSadBackground = BackgroundTheme(color = Slate20, tonalElevation = 1.dp)
val DarkAndroidSadBackground = BackgroundTheme(color = DarkBgSad, tonalElevation = 1.dp)

val DarkAndroidHappyBackground = BackgroundTheme(color = DarkBgHappy, tonalElevation = 3.dp)
val LightAndroidHappyBackground = BackgroundTheme(color = Amber10, tonalElevation = 3.dp)

val DarkAndroidAnxietyBackground = BackgroundTheme(color = DarkBgAnxiety, tonalElevation = 2.dp)
val LightAndroidAnxietyBackground = BackgroundTheme(color = Purple10, tonalElevation = 2.dp)


private fun enableAndroidMoodDarkGradient(mood: Mood,darkTheme: Boolean) : GradientColors {
    return when(mood) {
        Mood.Normal -> if (darkTheme) DarkAndroidNormalGradientColor else LightAndroidNormalGradientColor
        Mood.Sad -> if (darkTheme) DarkAndroidSadGradientColor else LightAndroidSadGradientColor
        Mood.Anxiety -> if (darkTheme) DarkAndroidAnxietyGradientColor else LightAndroidAnxietyGradientColor
        Mood.Happy -> if (darkTheme) DarkAndroidHappyGradientColor else LightAndroidHappyGradientColor
    }
}

private fun enableDefaultMoodDarkTheme(mood: Mood,darkTheme: Boolean) : ColorScheme {
    return when(mood) {
        Mood.Normal -> if (darkTheme) DarkDefaultNormalColorTheme else LightDefaultNormalColorTheme
        Mood.Sad -> if (darkTheme) DarkDefaultSadColorTheme else LightDefaultSadColorTheme
        Mood.Anxiety -> if (darkTheme) DarkDefaultAnxietyColorTheme else LightDefaultAnxietyColorTheme
        Mood.Happy -> if (darkTheme) DarkDefaultHappyColorTheme else LightDefaultHappyColorTheme
    }
}
private fun enableAndroidMoodDarkTheme(mood: Mood,darkTheme: Boolean) : ColorScheme {
    return when(mood) {
        Mood.Normal -> if (darkTheme) DarkAndroidNormalColorTheme else LightAndroidNormalColorTheme
        Mood.Sad -> if (darkTheme) DarkAndroidSadColorTheme else LightAndroidSadColorTheme
        Mood.Anxiety -> if (darkTheme) DarkAndroidAnxietyColorTheme else LightAndroidAnxietyColorTheme
        Mood.Happy -> if (darkTheme) DarkAndroidHappyColorTheme else LightAndroidHappyColorTheme
    }
}

private fun enableAndroidMoodDarkBackground(mood: Mood,darkTheme: Boolean) : BackgroundTheme {
    return when(mood) {
        Mood.Normal -> if (darkTheme) DarkAndroidNormalBackground else LightAndroidNormalBackground
        Mood.Sad -> if (darkTheme) DarkAndroidSadBackground else LightAndroidSadBackground
        Mood.Anxiety -> if (darkTheme) DarkAndroidAnxietyBackground else LightAndroidAnxietyBackground
        Mood.Happy -> if (darkTheme) DarkAndroidHappyBackground else LightAndroidHappyBackground
    }
}
@Composable
fun AIForumTheme(
    darkTheme : Boolean = isSystemInDarkTheme(),
    androidTheme : Boolean =false,
    disableDynamicTheming : Boolean =true,
    mood : Mood = Mood.Normal,
    content : @Composable () -> Unit

) {
    val colorScheme = when {
        androidTheme -> enableAndroidMoodDarkTheme(mood,darkTheme)
        !disableDynamicTheming && supportsDynamicTheming() -> {
            val context = LocalContext.current
            if (darkTheme) dynamicDarkColorScheme(context) else dynamicLightColorScheme(context)
        }
        else -> enableDefaultMoodDarkTheme(mood,darkTheme)
    }
    val tintTheme = when {
        androidTheme -> TintTheme()
        !disableDynamicTheming && supportsDynamicTheming() -> TintTheme(colorScheme.primary)
        else -> TintTheme()
    }
    val emptyGradientColors = GradientColors(container = colorScheme.surfaceColorAtElevation(2.dp))
    val defaultGradientColors = GradientColors(
        top = colorScheme.onSurface,
        bottom = colorScheme.primaryContainer,
        container = colorScheme.surface,
    )
    val gradientColor = when {
        androidTheme -> enableAndroidMoodDarkGradient(mood,darkTheme)
        !disableDynamicTheming && supportsDynamicTheming() -> emptyGradientColors
        else -> defaultGradientColors
    }
    val defaultBackgroundTheme = BackgroundTheme(
        color = colorScheme.surface,
        tonalElevation = 2.dp,
    )
    val backgroundTheme = when {
        androidTheme -> enableAndroidMoodDarkBackground(mood,darkTheme)
        else -> defaultBackgroundTheme
    }
    CompositionLocalProvider(
        LocalMood provides mood,
        LocalTintTheme provides tintTheme,
        LocalGradientColors provides gradientColor,
        LocalBackgroundTheme provides backgroundTheme

    ) {
        MaterialTheme(
            content = content,
            colorScheme = colorScheme,
            typography = AIForumTypography
        )
    }

}
@ChecksSdkIntAtLeast(api = Build.VERSION_CODES.S)
fun supportsDynamicTheming() = Build.VERSION.SDK_INT >= Build.VERSION_CODES.S
