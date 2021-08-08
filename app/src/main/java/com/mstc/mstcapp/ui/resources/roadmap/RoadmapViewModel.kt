package com.mstc.mstcapp.ui.resources.roadmap

import android.content.Context
import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.mstc.mstcapp.data.ResourceRepository
import com.mstc.mstcapp.data.STCDatabase
import com.mstc.mstcapp.model.resource.Roadmap

class RoadmapViewModel : ViewModel() {
    fun getRoadmap(context: Context, domain: String): LiveData<Roadmap> {
        val repository =
            ResourceRepository(context, STCDatabase.getInstance(context))
        return repository.getDomainRoadmap(domain)
    }

    fun refreshRoadmap(context: Context, domain: String) {
        val repository =
            ResourceRepository(context, STCDatabase.getInstance(context))
        repository.refreshRoadmap(domain)
    }

}