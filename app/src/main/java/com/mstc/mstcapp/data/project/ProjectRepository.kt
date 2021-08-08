package com.mstc.mstcapp.data.project

import android.util.Log
import androidx.paging.ExperimentalPagingApi
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import com.mstc.mstcapp.util.RetrofitService
import com.mstc.mstcapp.model.explore.Project
import kotlinx.coroutines.flow.Flow

private const val TAG = "ProjectRepository"

class ProjectRepository(
    private val service: RetrofitService,
    private val projectDatabase: ProjectDatabase,
) {
    fun getProjects(): Flow<PagingData<Project>> {
        Log.i(TAG, "getProjects: ")
        val pagingSourceFactory = { projectDatabase.projectDao().getProjects() }
        @OptIn(ExperimentalPagingApi::class)

        return Pager(
            config = PagingConfig(pageSize = NETWORK_PAGE_SIZE, enablePlaceholders = false),
            remoteMediator = ProjectRemoteMediator(
                service,
                projectDatabase
            ),
            pagingSourceFactory = pagingSourceFactory
        ).flow
    }

    companion object {
        const val NETWORK_PAGE_SIZE = 5
    }
}