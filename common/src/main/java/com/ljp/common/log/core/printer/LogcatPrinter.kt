package com.ljp.common.log.core.printer

import android.util.Log
import com.ljp.common.log.core.priority.LogcatPriority

class LogcatPrinter : LogPrinter {

    override fun print(priority: Int, tag: String?, msg: String) {
        Log.println(priority, tag, msg)
    }
}
