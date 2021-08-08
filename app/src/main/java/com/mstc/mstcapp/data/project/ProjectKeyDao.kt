package com.mstc.mstcapp.data.project

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.mstc.mstcapp.model.remoteKeys.ProjectRemoteKey

@Dao
interface ProjectKeyDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(remoteKey: List<ProjectRemoteKey>)

    @Query("SELECT * FROM PROJECT_REMOTE_KEYS WHERE projectId = :projectId")
    suspend fun projectRemoteKeysId(projectId: String): ProjectRemoteKey?

    @Query("DELETE FROM PROJECT_REMOTE_KEYS")
    suspend fun clearAll()
}