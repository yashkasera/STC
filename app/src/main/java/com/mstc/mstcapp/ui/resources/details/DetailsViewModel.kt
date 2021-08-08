package com.mstc.mstcapp.ui.resources.details

import android.content.Context
import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.mstc.mstcapp.data.ResourceRepository
import com.mstc.mstcapp.data.STCDatabase
import com.mstc.mstcapp.model.resource.Detail

class DetailsViewModel() : ViewModel() {

    fun getDetails(context: Context, domain: String): LiveData<Detail> {
        val repository =
            ResourceRepository(context, STCDatabase.getInstance(context))
        return repository.getDomainDetails(domain)
    }

    fun refreshDetails(context: Context, domain: String) {
        val repository =
            ResourceRepository(context, STCDatabase.getInstance(context))
        repository.refreshDetails(domain)
    }

}