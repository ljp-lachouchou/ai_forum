package ai.ljp.aiforum

import ai.ljp.sync.initializer.Sync
import ai.ljp.sync.worker.SyncWorker
import android.app.Application
import cn.jpush.android.api.JPushInterface
import com.google.firebase.FirebaseApp
import dagger.hilt.android.HiltAndroidApp
import javax.inject.Inject

@HiltAndroidApp
class AIForumApp : Application() {
    override fun onCreate() {
        super.onCreate()
        FirebaseApp.initializeApp(this)
        JPushInterface.setDebugMode(true)
        JPushInterface.init(this)
        Sync.initialize(this)
    }
}