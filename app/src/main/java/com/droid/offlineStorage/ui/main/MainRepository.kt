package com.droid.offlineStorage.ui.main

import android.util.Log
import androidx.lifecycle.LiveData
import com.droid.offlineStorage.local.ExampleDao
import com.droid.offlineStorage.model.ExResponse
import com.droid.offlineStorage.retrofit.RestApi
import com.droid.offlineStorage.utils.CommonUtils
import com.google.gson.Gson
import kotlinx.coroutines.flow.*
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

/**
 * Created by Akhtar
 */
class MainRepository(private val exampleDao: ExampleDao) {

    suspend fun insert(exResponse: ExResponse) {
        exampleDao.insertExample(exResponse)
    }

    suspend fun update(exResponse: ExResponse) {
        exampleDao.updateExample(exResponse)
    }

    suspend fun delete(exResponse: ExResponse) {
        exampleDao.deleteExample(exResponse)
    }

    suspend fun updateById(id: Int, updatedOn: String, title: String, description: String) {
        exampleDao.updateById(id, updatedOn, title, description)
    }

    suspend fun updateGeo(lat: Double, lng: Double) {
        exampleDao.updateGeo(lat, lng)
    }

    /*fun getDataByIdLive(id: Int): LiveData<ExResponse> {
        return exampleDao.getDataById(id)
    }*/

    suspend fun getDataById(id: Int): ExResponse {
        return exampleDao.getDataById(id)
    }

    /*Delete All From DataBase*/
    fun deleteAllFromDB() {
        exampleDao.deleteAll()
    }

    /*Delete By Id From DataBase*/
    fun deleteByIdFromDB(id: Int){
        exampleDao.deleteById(id)
    }


    /*Get All data as List*/
    suspend fun getFromDb(): List<ExResponse> {
        return exampleDao.getAllFromList()
    }


    /*Get All data as LD*/
    fun getFromDbAsLD(): LiveData<List<ExResponse>> {
        return exampleDao.getAllFromListAsLD()
    }


    suspend fun apiCallAndPutInDB(lat: Double = 0.0, lng: Double = 0.0) {
        val gson = Gson()
        val retrofit = Retrofit.Builder()
            .addConverterFactory(GsonConverterFactory.create(gson))
            .baseUrl("https://jsonplaceholder.typicode.com/")
            .build()
        val restApi = retrofit.create(RestApi::class.java)


        val dt = CommonUtils.getDateAndTime()
        val response = restApi.getAllPosts()
        if (response.isSuccessful) {
            response.body()?.let {
                it.asFlow()
                    .map {
                        it.createdOn = dt
                        it.updateOn = dt
                        it.lat = lat
                        it.lng = lng
                    }
                    .collect {
                        Log.e("TAG", "Updating ")
                    }
                Log.e("TAG", "Updatedddd ")
                exampleDao.deleteAll()
                exampleDao.insertExamples(it)
            }
        } else {
            Log.e("TAG", "OOPS!! something went wrong..")
        }

        /*flowOf(restApi.getAllPosts())
            .map {
                if (it.isSuccessful) {

                }else {
                    Log.e("TAG", "OOPS!! something went wrong..")
                }
            }*/
    }
}