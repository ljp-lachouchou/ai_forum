package ai.ljp.logger.di

import javax.inject.Qualifier

@Qualifier
@Retention(AnnotationRetention.RUNTIME)
annotation class LogPrinterConfig(
    val logger : AIForumLogger
)
enum class AIForumLogger{
    Logcat,
}


