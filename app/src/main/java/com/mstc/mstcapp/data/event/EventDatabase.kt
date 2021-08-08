package com.mstc.mstcapp.data.event

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.mstc.mstcapp.model.explore.Event
import com.mstc.mstcapp.model.remoteKeys.EventRemoteKey

@Database(
    entities = [
        EventRemoteKey::class,
        Event::class],
    version = 1,
    exportSchema = false
)
abstract class EventDatabase : RoomDatabase() {

    abstract fun eventDao(): EventDao
    abstract fun eventKeyDao(): EventKeyDao

    companion object {

        @Volatile
        private var INSTANCE: EventDatabase? = null

        fun getInstance(context: Context): EventDatabase =
            INSTANCE ?: synchronized(this) {
                INSTANCE
                    ?: buildDatabase(context).also { INSTANCE = it }
            }

        private fun buildDatabase(context: Context) =
            Room.databaseBuilder(
                context.applicationContext,
                EventDatabase::class.java, "EventDatabase.db"
            )
                .build()
    }
}