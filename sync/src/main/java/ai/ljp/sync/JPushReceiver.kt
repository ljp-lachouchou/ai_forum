package ai.ljp.sync

import ai.ljp.sync.status.SyncManager
import android.content.Context
import android.util.Log
import cn.jpush.android.api.CustomMessage
import cn.jpush.android.service.JPushMessageReceiver
import dagger.hilt.EntryPoint
import dagger.hilt.InstallIn
import dagger.hilt.android.EntryPointAccessors
import dagger.hilt.components.SingletonComponent

class JPushReceiver : JPushMessageReceiver() {
    @EntryPoint
    @InstallIn(SingletonComponent::class)
    interface SyncManagerEntryPoint {
        fun syncManager(): SyncManager
    }
    override fun onMessage(p0: Context?, p1: CustomMessage?) {
        Log.d("JPush", "收到同步指令: ${p1?.message}")
        val entryPoint = EntryPointAccessors.fromApplication(
            p0!!.applicationContext,
        SyncManagerEntryPoint::class.java
        )
        val syncManager = entryPoint.syncManager()
        // 触发你的同步逻辑
        syncManager.requestSync()
    }
}