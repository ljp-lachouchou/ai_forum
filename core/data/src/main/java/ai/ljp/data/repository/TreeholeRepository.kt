package ai.ljp.data.repository

import ai.ljp.data.Syncable
import androidx.paging.PagingData
import com.ljp.model.Treehole
import com.ljp.model.TreeholeProfileSource
import kotlinx.coroutines.flow.Flow

interface TreeholeRepository : Syncable {
    suspend fun createTreehole(
        content: String,
        isAnonymous: Boolean
    ) : Boolean

    fun getTreeholes() : Flow<PagingData<TreeholeProfileSource>>
}