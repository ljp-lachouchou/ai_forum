package ai.ljp.data.repository.impl

import ai.ljp.data.FeedPagingConfig
import ai.ljp.data.model.AISearch
import ai.ljp.data.model.asExtraModel
import ai.ljp.data.repository.InteractionWordRepository
import ai.ljp.database.dao.WordDao
import ai.ljp.database.model.PopulatedWordCommentsResource
import ai.ljp.database.model.asExtraModel
import ai.ljp.datastore.AIForumPreferencesDatastore
import ai.ljp.network.AIForumNetworkDataSource
import ai.ljp.network.model.SearchResult
import androidx.paging.Pager
import androidx.paging.PagingData
import androidx.paging.map
import com.ljp.model.WordCommentsResource
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class OfflineFirstInteractionWordRepository @Inject constructor(
    private val wordDao: WordDao,
    private val network: AIForumNetworkDataSource,
    private val preferencesDatastore: AIForumPreferencesDatastore
) : InteractionWordRepository{
    override fun observerAllWords(): Flow<PagingData<WordCommentsResource>> =
        Pager(
            config = FeedPagingConfig,
            pagingSourceFactory = {
                wordDao.getPopulatedWordResources()
            }
        )
            .flow
            .map { pagingData->
                pagingData.map(PopulatedWordCommentsResource::asExtraModel)
            }

    override suspend fun aiSearch(
        query: String
    ): AISearch {
        val userData = preferencesDatastore.userData.first()
        val aiSearchResp = network.aiSearch(
            userId = userData.currentUserId!!,
            query = query
        ) ?: return AISearch(searchOk = false)
        val wordItems = aiSearchResp.referenceWords.sortedBy(SearchResult::score)
            .map(SearchResult::wordId)
            .let {wordIds ->
                network.syncWords(ids = wordIds)
            }?.map { wordItem->
                wordItem.asExtraModel()
            }
        return AISearch(aiWordItems = wordItems,searchOk = true, answer = aiSearchResp.answer)
    }

    override fun getSelfWords(profileId: String): Flow<PagingData<WordCommentsResource>> =
        Pager(
            config = FeedPagingConfig,
            pagingSourceFactory = {
                wordDao.getSelfWords(profileId)
            }
        )
            .flow
            .map { pagingData->
                pagingData.map(PopulatedWordCommentsResource::asExtraModel)
            }

    override fun getPost(wordId: String): Flow<WordCommentsResource> =
        wordDao.getPost(wordId).map(PopulatedWordCommentsResource::asExtraModel)


}