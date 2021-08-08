package com.mstc.mstcapp.ui.explore.about

import android.content.Context
import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.mstc.mstcapp.data.ResourceRepository
import com.mstc.mstcapp.data.STCDatabase
import com.mstc.mstcapp.model.explore.BoardMember

class AboutViewModel : ViewModel() {
    fun getBoard(context: Context): LiveData<List<BoardMember>> {
        val repository =
            ResourceRepository(context, STCDatabase.getInstance(context))
        return repository.getBoardMembers()
    }

    fun refreshBoard(context: Context) {
        val repository =
            ResourceRepository(context, STCDatabase.getInstance(context))
        repository.refreshBoard()
    }
}