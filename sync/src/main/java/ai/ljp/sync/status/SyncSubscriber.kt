package ai.ljp.sync.status

interface SyncSubscriber {
    suspend fun subscribe()
}