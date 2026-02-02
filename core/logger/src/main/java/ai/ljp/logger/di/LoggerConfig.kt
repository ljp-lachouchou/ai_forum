package ai.ljp.logger.di

import javax.inject.Qualifier

@Qualifier
@Retention(AnnotationRetention.RUNTIME)
annotation class LoggerConfig(
    val logger : AIForumLogger
)