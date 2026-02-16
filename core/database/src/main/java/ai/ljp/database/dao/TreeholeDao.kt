package ai.ljp.database.dao

import ai.ljp.database.model.PopulateTreeholeEntity
import ai.ljp.database.model.TreeholeEntity
import androidx.paging.PagingSource
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy.Companion.IGNORE
import androidx.room.Query
import androidx.room.Transaction
import androidx.room.Upsert

@Dao
interface TreeholeDao {
    @Upsert
    suspend fun upsertTreeholes(treeholes : List<TreeholeEntity>)
    @Insert(onConflict = IGNORE)
    suspend fun insertTreehole(treehole : TreeholeEntity)
    @Transaction

    @Query("""
        SELECT * FROM treeholes
        ORDER BY createdAt DESC
    """)
    fun getTreeholes() : PagingSource<Int, PopulateTreeholeEntity>

    @Query(
        value = """
            DELETE FROM treeholes
            WHERE `treeholeId` in (:ids)
        """,
    )
    suspend fun deleteAll(ids : List<String>)
}