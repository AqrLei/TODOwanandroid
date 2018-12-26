package com.aqrlei.open.todowanandroid.base

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MediatorLiveData
import androidx.lifecycle.MutableLiveData
import com.aqrlei.open.retrofit.livedatacalladapter.LiveObservable
import com.aqrlei.open.retrofit.livedatacalladapter.LiveResponse
import com.aqrlei.open.todowanandroid.net.resp.BaseRespBean

/**
 * @author aqrlei on 2018/12/24
 */

abstract class BaseViewModel(application: Application) : AndroidViewModel(application) {
    val toast = MutableLiveData<String>()
    val isLoading = MutableLiveData<Boolean>()
    private val commonObserver = MediatorLiveData<Any>()


    protected fun <T> observerRespData(
        liveObservable: LiveObservable<LiveResponse<BaseRespBean<T>>>,
        isShowLoading: Boolean,
        processDataAction: (T) -> Unit) {

      /*  liveObservable.observable(commonObserver){_,_

        }*/
    }
}