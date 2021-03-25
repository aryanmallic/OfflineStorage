package com.droid.offlineStorage.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.google.gson.annotations.SerializedName

/**
 * Created by Akhtar
 */
@Entity(tableName = "example_post_table")
data class ExResponse(

    @ColumnInfo(name = "example_user_id")
    @SerializedName("userId")
    val userId: Int = 0,

    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "example_id")
    @SerializedName("id")
    val id: Int = 0,

    @ColumnInfo(name = "example_title")
    @SerializedName("title")
    val title: String = "",

    @ColumnInfo(name = "example_body")
    @SerializedName("body")
    val body: String = "",

    @ColumnInfo(name = "example_lat")
    var lat: Double = 0.0,

    @ColumnInfo(name = "example_lng")
    var lng: Double = 0.0,

    @ColumnInfo(name = "example_created_on")
    var createdOn: String = "",

    @ColumnInfo(name = "example_updated_on")
    var updateOn: String = ""
)