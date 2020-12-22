package com.simcik.branchassessment.activities

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData

class SharedViewModel(application: Application) : AndroidViewModel(application) {
    private var mutableLiveData = MutableLiveData<String>()
    var title: LiveData<String> = mutableLiveData

    fun swapTitle(string: String) {
        mutableLiveData.value = string
    }

}