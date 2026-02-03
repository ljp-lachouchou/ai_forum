package ai.ljp.database.util

import ai.ljp.database.model.help.SyncState
import androidx.room.TypeConverter

internal class SyncStateConverter {
    @TypeConverter
    fun syncStateToInt(syncState : SyncState?) : Int? =
        syncState?.let {
            when(it) {
                SyncState.Pending -> 0
                SyncState.Fail -> 2
                SyncState.Success -> 1
            }
        }
    @TypeConverter
    fun intToSyncState(int : Int?) : SyncState? =
        int?.let {
            when(int) {
                0 -> SyncState.Pending
                1 -> SyncState.Success
                2 -> SyncState.Fail
                else -> null
            }
        }
}