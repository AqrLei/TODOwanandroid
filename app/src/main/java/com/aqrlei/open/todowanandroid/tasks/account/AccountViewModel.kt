package com.aqrlei.open.todowanandroid.tasks.account

import android.app.Application
import android.util.Log
import androidx.lifecycle.MutableLiveData
import com.aqrlei.open.todowanandroid.base.BaseViewModel
import com.aqrlei.open.todowanandroid.net.repository.AccountRepository
import com.aqrlei.open.utils.ActivityCollector

/**
 * @author aqrlei on 2018/12/25
 */
class AccountViewModel(application: Application) : BaseViewModel(application) {

    private val accountRepo = AccountRepository()
    val userNameLiveData = MutableLiveData<String>()
    val passwordLiveData = MutableLiveData<String>()
    val rePasswordLiveData = MutableLiveData<String>()


    private val userName: String
        get() = userNameLiveData.value.orEmpty()
    private val password: String
        get() = passwordLiveData.value.orEmpty()
    private val rePassword: String
        get() = rePasswordLiveData.value.orEmpty()

    fun login() {
        observerRespData(accountRepo.login(userName, password), true, {
            // TODO handle login success result
            Log.d("ViewModelTest", it.userName.orEmpty())
        })
    }

    fun register() {
        observerRespData(accountRepo.register(userName, password, rePassword), true, {
            //TODO handle register
        })
    }

    fun logout() {
        observerRespData(accountRepo.logout(), false, {
            //TODO handle logout
            ActivityCollector.killApp()
        })
    }


}