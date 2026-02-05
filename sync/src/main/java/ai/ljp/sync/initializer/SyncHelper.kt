package ai.ljp.sync.initializer

import android.net.NetworkRequest
import androidx.work.Constraints
import androidx.work.NetworkType

const val SYNC_TOPIC = "sync"

internal const val SYNC_WORK_NAME = "SyncWorkName"

val SyncConstraints
    get() = Constraints.Builder()
        .setRequiredNetworkType(NetworkType.CONNECTED)
        .build()