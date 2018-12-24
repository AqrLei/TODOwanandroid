package com.aqrlei.open.todowanandroid.base

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData

/**
 * @author aqrlei on 2018/12/24
 */

abstract class BaseViewModel(application: Application) : AndroidViewModel(application) {
    val toast = MutableLiveData<String>()
    val isLoading = MutableLiveData<Boolean>()
}