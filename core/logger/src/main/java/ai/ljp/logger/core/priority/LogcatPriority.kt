package ai.ljp.logger.core.priority

import android.util.Log
import ai.ljp.logger.core.printer.LogcatPrinter

class LogcatPriority(
    override val printer: LogcatPrinter,
    override val infoPriority: Int,
    override val debugPriority: Int,
    override val errorPriority: Int,
    override val warnPriority: Int,
    override val assertPriority: Int,
    override val verbosePriority: Int

) : BaseLogPriority<LogcatPrinter>()
