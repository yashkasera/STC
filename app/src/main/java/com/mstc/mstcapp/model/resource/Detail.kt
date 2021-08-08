package com.mstc.mstcapp.model.resource

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.google.gson.annotations.SerializedName

@Entity(tableName = "DETAILS")
class Detail(
    @SerializedName("description")
    val description: String,
    @SerializedName("domain")
    val domain: String,

    @SerializedName("expectation")
    val expectation: String,

    @PrimaryKey
    @SerializedName("_id")
    val id: String,
)