package com.aqrlei.open.todowanandroid.tasks.account

import android.app.Application
import android.util.Log
import androidx.lifecycle.MutableLiveData
import com.aqrlei.open.todowanandroid.base.BaseViewModel
import com.aqrlei.open.todowanandroid.net.repository.AccountRepository

/**
 * @author aqrlei on 2018/12/25
 */
class LoginViewModel(application: Application) : BaseViewModel(application) {

    private val accountRepo = AccountRepository()
    val userNameLiveData = MutableLiveData<String>()
    fun login() {
        val userName: String = "AqrAirSigns";
        val password: String = "Aqrwanandroid520"
        observerRespData(accountRepo.login(userName, password), true, {
            userNameLiveData.value = it.userName.orEmpty()
            Log.d("ViewModelTest", it.userName.orEmpty())
        })
    }


}