package com.mstc.mstcapp.data.project

import androidx.paging.PagingSource
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.mstc.mstcapp.model.explore.Project

@Dao
interface ProjectDao {
    @Query("SELECT * FROM PROJECTS ORDER BY id DESC")
    fun getProjects(): PagingSource<Int, Project>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertProjectProjectDao(project: Project)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(projects: List<Project>)

    @Query("DELETE FROM PROJECTS")
    suspend fun clearAll()

}