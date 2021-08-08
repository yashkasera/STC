package com.mstc.mstcapp.data.project

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.mstc.mstcapp.model.explore.Project
import com.mstc.mstcapp.model.remoteKeys.ProjectRemoteKey

@Database(
    entities = [
        ProjectRemoteKey::class,
        Project::class],
    version = 1,
    exportSchema = false
)
abstract class ProjectDatabase : RoomDatabase() {

    abstract fun projectDao(): ProjectDao
    abstract fun projectKeyDao(): ProjectKeyDao

    companion object {

        @Volatile
        private var INSTANCE: ProjectDatabase? = null

        fun getInstance(context: Context): ProjectDatabase =
            INSTANCE ?: synchronized(this) {
                INSTANCE
                    ?: buildDatabase(context).also { INSTANCE = it }
            }

        private fun buildDatabase(context: Context) =
            Room.databaseBuilder(
                context.applicationContext,
                ProjectDatabase::class.java, "ProjectDatabase.db"
            )
                .build()
    }
}