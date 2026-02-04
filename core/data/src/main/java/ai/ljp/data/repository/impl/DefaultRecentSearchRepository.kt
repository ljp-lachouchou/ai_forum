package ai.ljp.data.repository.impl

import ai.ljp.data.repository.RecentSearchRepository
import ai.ljp.database.dao.RecentSearchQueryDao
import ai.ljp.database.model.RecentSearchQueryEntity
import ai.ljp.database.model.asExternalModel
import com.ljp.model.RecentSearchQuery
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.datetime.Clock
import javax.inject.Inject

class DefaultRecentSearchRepository @Inject constructor(
    private val recentSearchQueryDao: RecentSearchQueryDao
) : RecentSearchRepository {
    override fun getRecentSearchQueries(limit: Int): Flow<List<RecentSearchQuery>> =
        recentSearchQueryDao.getRecentSearchQueryEntities(limit).map { searchQueryEntities ->
            searchQueryEntities.map { it.asExternalModel() }
        }

    override suspend fun insertOrReplaceRecentSearch(searchQuery: String) {
        recentSearchQueryDao.insertOrReplaceRecentSearchQuery(
            RecentSearchQueryEntity(
                query = searchQuery,
                queriedDate = Clock.System.now()
            )
        )
    }

    override suspend fun clearRecentSearches() = recentSearchQueryDao.clearRecentSearchQueries()
}