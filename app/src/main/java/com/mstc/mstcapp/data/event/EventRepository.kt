package com.mstc.mstcapp.data.event

import android.util.Log
import androidx.paging.ExperimentalPagingApi
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import com.mstc.mstcapp.util.RetrofitService
import com.mstc.mstcapp.model.explore.Event
import kotlinx.coroutines.flow.Flow

private const val TAG = "EventRepository"

class EventRepository(
    private val service: RetrofitService,
    private val eventDatabase: EventDatabase,
) {
    fun getEvents(): Flow<PagingData<Event>> {
        Log.i(TAG, "getEvents: ")
        val pagingSourceFactory = { eventDatabase.eventDao().getEvents() }
        @OptIn(ExperimentalPagingApi::class)
        return Pager(
            config = PagingConfig(pageSize = NETWORK_PAGE_SIZE, enablePlaceholders = false),
            remoteMediator = EventRemoteMediator(
                service,
                eventDatabase
            ),
            pagingSourceFactory = pagingSourceFactory
        ).flow
    }

    companion object {
        const val NETWORK_PAGE_SIZE = 5
    }
}