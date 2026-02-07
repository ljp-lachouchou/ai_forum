package ai.ljp.data

import androidx.paging.PagingConfig

val FeedPagingConfig : PagingConfig
    get() = PagingConfig(
        pageSize = 15,
        enablePlaceholders = false,
        initialLoadSize = 30
    )