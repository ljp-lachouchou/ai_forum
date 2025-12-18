package com.ljp.common.log.core.printer

import android.content.Context
import com.ljp.common.log.core.priority.BaseLogPriority
import com.ljp.common.log.core.priority.LogLevel



interface LogPrinter {
    fun print(priority: Int,tag:String?,msg: String)
}



