package com.mstc.mstcapp.model.remoteKeys

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "EVENT_REMOTE_KEYS")
data class EventRemoteKey(
    @PrimaryKey
    val eventId: String,
    val prevKey: Int?,
    val nextKey: Int?,
)