package ai.ljp.data.repository

import ai.ljp.data.Syncable
import androidx.paging.PagingData
import com.ljp.model.Treehole
import kotlinx.coroutines.flow.Flow

interface TreeholeRepository : Syncable {
    suspend fun upsertTreeholes(treeholes : List<Treehole>)

    suspend fun insertTreehole(treehole : Treehole)

    fun getTreeholes() : Flow<PagingData<Treehole>>
}