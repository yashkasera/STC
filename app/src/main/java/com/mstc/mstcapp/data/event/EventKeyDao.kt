package com.mstc.mstcapp.data.event

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.mstc.mstcapp.model.remoteKeys.EventRemoteKey
import com.mstc.mstcapp.model.remoteKeys.FeedRemoteKey

@Dao
interface EventKeyDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(remoteKey: List<EventRemoteKey>)

    @Query("SELECT * FROM EVENT_REMOTE_KEYS WHERE eventId = :eventId")
    suspend fun eventRemoteKeysId(eventId: String): EventRemoteKey?

    @Query("DELETE FROM EVENT_REMOTE_KEYS")
    suspend fun clearAll()
}