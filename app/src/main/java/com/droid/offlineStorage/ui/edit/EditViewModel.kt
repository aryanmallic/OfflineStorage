package com.droid.offlineStorage.ui.edit

import androidx.databinding.Bindable
import androidx.databinding.Observable
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.droid.offlineStorage.model.ExResponse
import com.droid.offlineStorage.ui.main.MainRepository
import com.droid.offlineStorage.utils.CommonUtils
import kotlinx.coroutines.*

/**
 * Created by Akhtar
 */
class EditViewModel(private val mainRepository: MainRepository) : ViewModel(), Observable {

    @Bindable
    val inputTitle = MutableLiveData<String>()

    @Bindable
    val inputDescription = MutableLiveData<String>()

    val curId = MutableLiveData<Int>()

    val isUpdated = MutableLiveData<Boolean>()

    init {
        isUpdated.value = false
    }

    fun getById(id: Int) {
        curId.value = id
        viewModelScope.launch(Dispatchers.IO) {
            val resp = mainRepository.getDataById(id)
            withContext(Dispatchers.Main) {
                initUpdate(resp)
            }
        }
    }

    private fun initUpdate(exResponse: ExResponse) {
        inputTitle.value = exResponse.title
        inputDescription.value = exResponse.body
    }

    fun saveAndUpdate() {
        viewModelScope.launch(Dispatchers.IO) {
            curId.value?.let {
                mainRepository.updateById(
                    id = it,
                    updatedOn = CommonUtils.getDateAndTime(),
                    inputTitle.value!!,
                    inputDescription.value!!
                )
                withContext(Dispatchers.Main) {
                    isUpdated.value = true
                }
            }
        }
    }

    override fun addOnPropertyChangedCallback(callback: Observable.OnPropertyChangedCallback?) {
    }

    override fun removeOnPropertyChangedCallback(callback: Observable.OnPropertyChangedCallback?) {
    }
}