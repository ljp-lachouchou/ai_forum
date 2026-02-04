package ai.ljp.sync.status

import ai.ljp.sync.initializer.SYNC_TOPIC
import com.google.firebase.messaging.FirebaseMessaging
import kotlinx.coroutines.tasks.await
import javax.inject.Inject

class FirebaseSubscriber @Inject constructor(
    private val firebaseMessaging: FirebaseMessaging
) : SyncSubscriber {
    override suspend fun subscribe()  {
        firebaseMessaging
            .subscribeToTopic(SYNC_TOPIC)
            .await()
    }
}