package com.mstc.mstcapp.ui.resources.resource

import android.content.Context
import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.mstc.mstcapp.data.ResourceRepository
import com.mstc.mstcapp.data.STCDatabase
import com.mstc.mstcapp.model.resource.Resource

class ResourceViewModel : ViewModel() {
    fun getResources(context: Context, domain: String): LiveData<List<Resource>> {
        val repository =
            ResourceRepository(context, STCDatabase.getInstance(context))
        return repository.getDomainResources(domain)
    }

    fun refreshResources(context: Context, domain: String) {
        val repository =
            ResourceRepository(context, STCDatabase.getInstance(context))
        repository.refreshResources(domain)
    }
}