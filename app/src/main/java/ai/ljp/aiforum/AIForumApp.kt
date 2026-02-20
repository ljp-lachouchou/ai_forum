package ai.ljp.aiforum

import ai.ljp.sync.initializer.Sync
import android.app.Application
import android.os.Build
import androidx.annotation.RequiresApi
import cn.jpush.android.api.JPushInterface
import dagger.hilt.android.HiltAndroidApp
import android.app.ActivityManager
import android.content.Context
import android.content.pm.ApplicationInfo
import android.os.Process
import android.os.StrictMode
import android.os.StrictMode.ThreadPolicy.Builder
import coil.ImageLoader
import coil.ImageLoaderFactory
import javax.inject.Inject

@HiltAndroidApp
class AIForumApp : Application(), ImageLoaderFactory {
    @RequiresApi(Build.VERSION_CODES.P)
    override fun onCreate() {
        super.onCreate()
        setStrictModePolicy()
        JPushInterface.setDebugMode(true)
        JPushInterface.init(this)
        if (isMainProcess()) {
            Sync.sync(this)
        }
    }
    @Inject
    lateinit var imageLoader: dagger.Lazy<ImageLoader>

    private fun isDebuggable(): Boolean {
        return 0 != applicationInfo.flags and ApplicationInfo.FLAG_DEBUGGABLE
    }
    private fun setStrictModePolicy() {
        if (isDebuggable()) {
            StrictMode.setThreadPolicy(
                Builder().detectAll().penaltyLog().build(),
            )
        }
    }

    override fun newImageLoader(): ImageLoader = imageLoader.get()
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