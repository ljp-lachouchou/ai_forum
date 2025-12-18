package com.ljp.common.log.core.priority

import android.util.Log
import com.ljp.common.log.core.printer.LogcatPrinter

class LogcatPriority(
    override val printer: LogcatPrinter,
    override val infoPriority: Int,
    override val debugPriority: Int,
    override val errorPriority: Int,
    override val warnPriority: Int,
    override val assertPriority: Int,
    override val verbosePriority: Int

) : BaseLogPriority<LogcatPrinter>()
val LogcatPriorityInstance: LogcatPriority by lazy { LogcatPriority(
    LogcatPrinter(),
    Log.INFO,
    Log.DEBUG,
    Log.ERROR,
    Log.WARN,
    Log.ASSERT,
    Log.VERBOSE
) }