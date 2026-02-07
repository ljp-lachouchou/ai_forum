package ai.ljp.data.repository.impl

import ai.ljp.data.FeedPagingConfig
import ai.ljp.data.SYNC_BATCH_SIZE
import ai.ljp.data.Synchronizer
import ai.ljp.data.changeSync
import ai.ljp.data.model.AIPost
import ai.ljp.data.model.asDBModel
import ai.ljp.data.model.asExtraModel
import ai.ljp.data.repository.WordRepository
import ai.ljp.database.dao.WordDao
import ai.ljp.database.model.TreeholeEntity
import ai.ljp.database.model.WordEntity
import ai.ljp.database.model.asExtraModel
import ai.ljp.database.model.roomTableName
import ai.ljp.datastore.AIForumPreferencesDatastore
import ai.ljp.datastore.ChangeVersion
import ai.ljp.network.AIForumNetworkDataSource
import ai.ljp.network.model.SyncWordItem
import ai.ljp.network.model.WordUpdateRequesst
import androidx.paging.Pager
import androidx.paging.PagingData
import androidx.paging.map
import com.ljp.model.Word
import com.ljp.model.WordTag
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map
import javax.inject.Inject
import kotlin.collections.chunked

class OfflineFirstWordRepository @Inject constructor(
    private val network : AIForumNetworkDataSource,
    private val preferencesDatastore: AIForumPreferencesDatastore,
    private val wordDao: WordDao
) : WordRepository{
    override suspend fun createWord(
        wordUrl: String,
        category: String,
        tags: List<WordTag>,
        wordName: String
    ) : Boolean {
        val userData = preferencesDatastore.userData.first()
        network.apply {
            val wordDetailResponse = createWord(
                authorId = userData.currentUserId,
                wordUrl = wordUrl,
                category = category,
                tags = tags,
                wordName = wordName,
            ) ?: return false
            val submit = submitWord(
                wordId = wordDetailResponse.wordId,
                authorId = wordDetailResponse.authorId
            )
            val publish = publishWord(
                wordId = wordDetailResponse.wordId,
                adminId = wordDetailResponse.authorId,
            )
            return submit && publish
        }
    }

    override suspend fun updateWord(
        wordId: String,
        wordUpdateRequesst: WordUpdateRequesst
    ) {
        val userData = preferencesDatastore.userData.first()
        network.updateWord(
            authorId = userData.currentUserId,
            wordId = wordId,
            wordUpdateRequesst = wordUpdateRequesst
        )
    }


    override suspend fun deleteWord(wordId: String) =
        network.deleteWord(wordId)

    override fun getWords(): Flow<PagingData<Word>> =
        Pager(
            config = FeedPagingConfig,
            pagingSourceFactory = {
                wordDao.getWords()
            }
        )
            .flow
            .map { pagingData->
                pagingData.map(WordEntity::asExtraModel)
            }

    override suspend fun aiAssistPost(content: String): AIPost =
        network.aiAssistPost(content)?.asExtraModel() ?: AIPost(
            content = "",
            suggestions = emptyList()
        )

    override suspend fun syncWith(synchronizer: Synchronizer): Boolean =
        synchronizer.changeSync(
            networkEntity = TreeholeEntity::class,
            versionReader = ChangeVersion::syncVersion,
            changeFetcher = {sinceVersion ->
                network.getChangelogs(since = sinceVersion)
            },
            versionUpdater = {lastVersion ->
                ChangeVersion(syncVersion = 1L * lastVersion)
            },
            entityTagger = {entityClass ->
                entityClass.roomTableName
            },
            modelDeleter = wordDao::deleteAll,
            modelUpdater = { changedIds ->//分批
                changedIds.chunked(SYNC_BATCH_SIZE).forEach { ids ->
                    val items = network.syncWords(ids = ids)
                        ?.map(SyncWordItem::asDBModel) ?: return@forEach
                    wordDao.upsertAll(items)
                }
            }
        )

}