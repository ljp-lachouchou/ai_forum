package ai.ljp.aiforum

import ai.ljp.sync.initializer.Sync
import android.app.Application
import android.os.Build
import androidx.annotation.RequiresApi
import cn.jpush.android.api.JPushInterface
import dagger.hilt.android.HiltAndroidApp
import android.app.ActivityManager
import android.content.Context
import android.os.Process
@HiltAndroidApp
class AIForumApp : Application() {
    @RequiresApi(Build.VERSION_CODES.P)
    override fun onCreate() {
        super.onCreate()
        JPushInterface.setDebugMode(true)
        JPushInterface.init(this)
        if (isMainProcess()) {
            Sync.initialize(this)
        }
    }
}


fun Context.isMainProcess(): Boolean {
    val currentProcessName = getCurrentProcessName()
    return packageName == currentProcessName
}

fun Context.getCurrentProcessName(): String? {
    // 1. 优先使用 API 28+ 的方法
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
        return Application.getProcessName()
    }

    // 2. 兼容低版本：通过 ActivityManager 获取
    val am = getSystemService(Context.ACTIVITY_SERVICE) as? ActivityManager
    val runningProcesses = am?.runningAppProcesses
    if (runningProcesses != null) {
        val myPid = Process.myPid()
        for (processInfo in runningProcesses) {
            if (processInfo.pid == myPid) {
                return processInfo.processName
            }
        }
    }

    return null
}