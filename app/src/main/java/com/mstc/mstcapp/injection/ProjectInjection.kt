package com.mstc.mstcapp.injection

import android.content.Context
import androidx.lifecycle.ViewModelProvider
import androidx.savedstate.SavedStateRegistryOwner
import com.mstc.mstcapp.util.RetrofitService
import com.mstc.mstcapp.data.project.ProjectDatabase
import com.mstc.mstcapp.data.project.ProjectRepository
import com.mstc.mstcapp.ui.explore.project.ProjectViewModelFactory


/**
 * Class that handles object creation.
 * Like this, objects can be passed as parameters in the constructors and then replaced for
 * testing, where needed.
 */
object ProjectInjection {
    private fun provideGithubRepository(context: Context): ProjectRepository {
        return ProjectRepository(RetrofitService.create(), ProjectDatabase.getInstance(context))
    }

    fun provideViewModelFactory(
        context: Context,
        owner: SavedStateRegistryOwner,
    ): ViewModelProvider.Factory {
        return ProjectViewModelFactory(owner, provideGithubRepository(context))
    }
}
