package com.droid.offlineStorage.ui.edit

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.droid.offlineStorage.ui.main.MainRepository

/**
 * Created by Akhtar
 */
class EditViewModelFactory (private val mainRepository: MainRepository) : ViewModelProvider.Factory {
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(EditViewModel::class.java)) {
            return EditViewModel(mainRepository) as T
        }
        throw IllegalArgumentException("Unknown View Model")
    }
}