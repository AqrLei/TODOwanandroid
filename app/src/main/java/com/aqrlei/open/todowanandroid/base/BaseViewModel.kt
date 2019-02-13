package com.aqrlei.open.todowanandroid.base

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import com.aqrlei.open.retrofit.livedatacalladapter.LiveObservable
import com.aqrlei.open.retrofit.livedatacalladapter.LiveResponse
import com.aqrlei.open.todowanandroid.net.resp.BaseRespBean
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

/**
 * @author aqrlei on 2018/12/24
 */

abstract class BaseViewModel(application: Application) : AndroidViewModel(application) {
    val toast = MutableLiveData<String>()
    val isLoading = MutableLiveData<Boolean>()
    var navigator:CommonNavigator? = null

    protected fun <T> observerRespData(
        liveObservable: LiveObservable<LiveResponse<BaseRespBean<T>>>,
        isShowLoading: Boolean,
        processDataAction: (T) -> Unit,
        finishAction: (() -> Unit)? = null,
        successWithoutErrorAction: (() -> Unit)? = null,
        processErrorAction: ((String, String) -> Boolean)? = null,
        processFailureAction: ((Throwable?) -> Boolean)? = null){
        GlobalScope.launch (Dispatchers.Main) {
            if (isShowLoading) {
                isLoading.value = true
            }
            liveObservable.observe { data ->
                if (data?.isSuccess == true) {
                    if (data.response?.errorCode == "0") {
                        successWithoutErrorAction?.invoke()
                        data.response?.data?.run(processDataAction)
                    } else {
                        if (processErrorAction?.invoke(
                                data.response?.errorMsg.orEmpty(),
                                data.response?.errorCode.orEmpty()
                            ) != true
                        ) {
                            toast.value = data.response?.errorMsg.orEmpty()
                        }
                    }
                } else {
                    if (processFailureAction?.invoke(data?.error) != true) {
                        toast.value = data?.error?.message.orEmpty()
                    }
                }
                if (isShowLoading) {
                    isLoading.value = false
                }
                finishAction?.invoke()
            }
        }
    }

    fun back() {
        navigator?.back()
    }

    interface CommonNavigator {
        fun back()
    }
}