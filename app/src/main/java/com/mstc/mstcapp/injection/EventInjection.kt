package com.mstc.mstcapp.injection

import android.content.Context
import androidx.lifecycle.ViewModelProvider
import androidx.savedstate.SavedStateRegistryOwner
import com.mstc.mstcapp.util.RetrofitService
import com.mstc.mstcapp.data.event.EventDatabase
import com.mstc.mstcapp.data.event.EventRepository
import com.mstc.mstcapp.ui.explore.event.EventViewModelFactory


/**
 * Class that handles object creation.
 * Like this, objects can be passed as parameters in the constructors and then replaced for
 * testing, where needed.
 */
object EventInjection {
    private fun provideEventRepository(context: Context): EventRepository {
        return EventRepository(RetrofitService.create(), EventDatabase.getInstance(context))
    }

    fun provideViewModelFactory(
        context: Context,
        owner: SavedStateRegistryOwner,
    ): ViewModelProvider.Factory {
        return EventViewModelFactory(owner, provideEventRepository(context))
    }
}
