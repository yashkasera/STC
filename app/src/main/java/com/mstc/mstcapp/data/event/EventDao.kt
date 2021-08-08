package com.mstc.mstcapp.data.event

import androidx.paging.PagingSource
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.mstc.mstcapp.model.explore.Event

@Dao
interface EventDao {
    @Query("SELECT * FROM EVENTS ORDER BY id DESC")
    fun getEvents(): PagingSource<Int, Event>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertEventEventDao(event: Event)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(events: List<Event>)

    @Query("DELETE FROM EVENTS")
    suspend fun clearAll()

}