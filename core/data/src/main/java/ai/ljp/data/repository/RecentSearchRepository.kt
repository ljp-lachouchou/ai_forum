package ai.ljp.data.repository

import com.ljp.model.RecentSearchQuery
import kotlinx.coroutines.flow.Flow

interface RecentSearchRepository {
    fun getRecentSearchQueries(limit : Int) : Flow<List<RecentSearchQuery>>
    suspend fun insertOrReplaceRecentSearch(searchQuery : String)

    suspend fun clearRecentSearches()
}