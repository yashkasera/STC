package `in`.stcvit.stcapp.data.feed

import `in`.stcvit.stcapp.model.Feed
import `in`.stcvit.stcapp.util.RetrofitService
import androidx.paging.ExperimentalPagingApi
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import kotlinx.coroutines.flow.Flow

private const val TAG = "FeedRepository"

class FeedRepository(
    private val service: RetrofitService,
    private val feedDatabase: FeedDatabase,
) {
    fun getFeeds(): Flow<PagingData<Feed>> {
        val pagingSourceFactory = { feedDatabase.feedDao().getFeed() }
        @OptIn(ExperimentalPagingApi::class)
        return Pager(
            config = PagingConfig(
                pageSize = NETWORK_PAGE_SIZE,
                enablePlaceholders = false,
            ),
            remoteMediator = FeedRemoteMediator(
                service,
                feedDatabase
            ),
            pagingSourceFactory = pagingSourceFactory
        ).flow
    }

    companion object {
        const val NETWORK_PAGE_SIZE = 5
    }
}