package ai.ljp.aiforum

import android.app.Application
import cn.jpush.android.api.JPushInterface
import com.google.firebase.FirebaseApp
import dagger.hilt.android.HiltAndroidApp

@HiltAndroidApp
class AIForumApp : Application() {
    override fun onCreate() {
        super.onCreate()
        FirebaseApp.initializeApp(this)
        JPushInterface.setDebugMode(true)
        JPushInterface.init(this)
    }
}