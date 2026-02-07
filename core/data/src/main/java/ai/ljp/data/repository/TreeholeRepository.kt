package ai.ljp.data.repository

import ai.ljp.data.Syncable
import androidx.paging.PagingData
import com.ljp.model.Treehole
import kotlinx.coroutines.flow.Flow

interface TreeholeRepository : Syncable {
    suspend fun createTreehole(
        content: String,
        isAnonymous: Boolean
    )

    fun getTreeholes() : Flow<PagingData<Treehole>>
}