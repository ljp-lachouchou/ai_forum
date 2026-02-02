package ai.ljp.logger.di

import ai.ljp.logger.core.printer.LogPrinter
import ai.ljp.logger.core.printer.LogcatPrinter
import ai.ljp.logger.core.printer.e
import ai.ljp.logger.core.priority.BaseLogPriority
import ai.ljp.logger.core.priority.LogcatPriority
import android.util.Log
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import java.util.logging.Logger
import javax.inject.Singleton


@Module
@InstallIn(SingletonComponent::class)
object LoggerModule {
    @Provides
    @Singleton
    @LogPrinterConfig(AIForumLogger.Logcat)
    fun providerPrinter() : LogPrinter = LogcatPrinter()

    @Provides
    @Singleton
    @LoggerConfig(AIForumLogger.Logcat)
    fun providersLoggerPriority(
        @LogPrinterConfig(AIForumLogger.Logcat) loggerPrinter : LogPrinter
    ) : BaseLogPriority<LogcatPrinter>  = LogcatPriority(
        loggerPrinter as? LogcatPrinter ?: error("this printer's type is error"),
        Log.INFO,
        Log.DEBUG,
        Log.ERROR,
        Log.WARN,
        Log.ASSERT,
        Log.VERBOSE
    )
}
