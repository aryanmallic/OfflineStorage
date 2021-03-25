package com.droid.offlineStorage.local

import androidx.lifecycle.LiveData
import androidx.room.*
import com.droid.offlineStorage.model.ExResponse

/**
 * Created by Akhtar
 */

@Dao
interface ExampleDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertExample(exResponse: ExResponse): Long

    @Update
    suspend fun updateExample(exResponse: ExResponse): Int

    @Delete
    suspend fun deleteExample(exResponse: ExResponse): Int

    @Insert
    suspend fun insertExamples(exResponse: List<ExResponse>): List<Long>


    @Query("SELECT * FROM example_post_table WHERE example_id = :id")
    suspend fun getDataById(id: Int): ExResponse//LiveData<ExResponse>

    @Query("DELETE FROM example_post_table")
    fun deleteAll()


    @Query("DELETE FROM example_post_table WHERE example_id = :id")
    fun deleteById(id: Int)


    @Query("SELECT * FROM example_post_table")
    suspend fun getAllFromList(): List<ExResponse>


    @Query("SELECT * FROM example_post_table")
    fun getAllFromListAsLD(): LiveData<List<ExResponse>>


    @Query("UPDATE example_post_table SET example_created_on = :co , example_updated_on = :uo")
    fun updateColumn(co: String, uo: String)


    @Query("UPDATE example_post_table SET example_updated_on = :uo, example_title = :title, example_body = :des  WHERE example_id = :id")
    suspend fun updateById(id: Int, uo: String, title: String, des: String)


    @Query("UPDATE example_post_table SET example_lat = :lat, example_lng = :lng")
    suspend fun updateGeo(lat: Double, lng: Double)
}