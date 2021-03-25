package com.droid.offlineStorage.ui.main

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.droid.offlineStorage.model.ExResponse
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch

/**
 * Created by Akhtar
 */
class MainViewModel(private val mainRepository: MainRepository) : ViewModel() {

    fun updateValues() {
        ExResponse()
    }

    fun insert(exResponse: ExResponse): Job =
        viewModelScope.launch {
            mainRepository.insert(exResponse)
        }

    fun update(exResponse: ExResponse): Job =
        viewModelScope.launch {
            mainRepository.update(exResponse)
        }

    fun delete(exResponse: ExResponse): Job =
        viewModelScope.launch {
            mainRepository.delete(exResponse)
        }

    fun saveGeo(lat: Double, lng: Double) {
        viewModelScope.launch(Dispatchers.IO) {
            Log.d("TAG", "GPSTracker VM: $lat, $lng")
            Log.d("TAG", "GPSTracker VM before update listSize: " + mainRepository.getFromDb().size)
            mainRepository.updateGeo(lat, lng)
        }
    }


    ////////////// GetList From DataBase
    fun getListData(): LiveData<List<ExResponse>> {
        val da = MutableLiveData<List<ExResponse>>()
        viewModelScope.launch {
            da.value = mainRepository.getFromDb()
        }
        return da
    }

    fun getListDataAsLD():LiveData<List<ExResponse>>{
        return  mainRepository.getFromDbAsLD()
    }
    //////////////





    ///////////// Delete By Id in DataBase
    fun deleteById(id: Int){
        clearById(id)
    }

    private fun clearById(id: Int): Job = viewModelScope.launch(Dispatchers.IO) {
        mainRepository.deleteByIdFromDB(id)
    }
    /////////////





    ///////////// Clear ALL or Delete ALL
    fun clearOrDelete() {
        clearAll()
    }

    private fun clearAll(): Job = viewModelScope.launch(Dispatchers.IO) {
        mainRepository.deleteAllFromDB()
    }
    //////////////





    ////////////// Call Api And Store Data
    fun getPostsFromApiAndStore(lat: Double, lng: Double): Job =
        viewModelScope.launch(Dispatchers.IO) {
            mainRepository.apiCallAndPutInDB(lat,lng)
        }

    /*fun getPostsFromApiAndStore(): Job =
        viewModelScope.launch(Dispatchers.IO) {
            mainRepository.apiCallAndPutInDB()
        }*/
    ///////////////
}