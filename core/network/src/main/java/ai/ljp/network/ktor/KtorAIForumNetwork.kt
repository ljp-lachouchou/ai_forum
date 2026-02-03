package ai.ljp.network.ktor

import ai.ljp.network.AIForumNetworkDataSource
import io.ljp.simapi.ApiClient
import javax.inject.Inject

class KtorAIForumNetwork @Inject constructor(
    val apiClient : ApiClient
) : AIForumNetworkDataSource {

}