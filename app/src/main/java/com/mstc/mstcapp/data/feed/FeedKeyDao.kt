package com.mstc.mstcapp.data.feed

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.mstc.mstcapp.model.remoteKeys.FeedRemoteKey

@Dao
interface FeedKeyDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(remoteKey: List<FeedRemoteKey>)

    @Query("SELECT * FROM FEED_REMOTE_KEYS WHERE feedId = :feedId")
    suspend fun feedRemoteKeysId(feedId: String): FeedRemoteKey?

    @Query("DELETE FROM FEED_REMOTE_KEYS")
    suspend fun clearAll()
}