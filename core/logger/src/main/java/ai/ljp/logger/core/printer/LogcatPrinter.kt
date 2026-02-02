package ai.ljp.logger.core.printer

import android.util.Log

class LogcatPrinter : LogPrinter {

    override fun print(priority: Int, tag: String?, msg: String) {
        Log.println(priority, tag, msg)
    }
}
