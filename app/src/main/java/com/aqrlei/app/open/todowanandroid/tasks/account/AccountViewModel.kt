package com.aqrlei.app.open.todowanandroid.tasks.account

import android.app.Application
import androidx.lifecycle.MutableLiveData
import com.aqrlei.app.open.todowanandroid.net.repository.AccountRepository
import com.aqrlei.open.utils.AppCache

/**
 * @author aqrlei on 2018/12/25
 */
class AccountViewModel(application: Application) :
    com.aqrlei.app.open.todowanandroid.base.BaseViewModel(application) {

    private val accountRepo = AccountRepository()
    val userNameLiveData = MutableLiveData<String>()
    val passwordLiveData = MutableLiveData<String>()
    val rePasswordLiveData = MutableLiveData<String>()

    val userNameErrorLiveData = MutableLiveData<String>()
    val passwordErrorLiveData = MutableLiveData<String>()
    val rePasswordErrorLiveData = MutableLiveData<String>()


    private val userName: String
        get() = userNameLiveData.value.orEmpty().trim()
    private val password: String
        get() = passwordLiveData.value.orEmpty().trim()
    private val rePassword: String
        get() = rePasswordLiveData.value.orEmpty().trim()

    private val accountNavigator: AccountNavigator?
        get() = navigator as? AccountNavigator

    fun login() {
        if (verifyAccount(false)) {
            observerRespData(accountRepo.login(userName, password), true, {
                AppCache.get()
                    .putString(com.aqrlei.app.open.todowanandroid.CacheConstant.USER_NAME_KEY, it.userName.orEmpty())
                    .commit()
                accountNavigator?.loginSuccess(it.userName.orEmpty())
            })
        }
    }

    private fun resetErrorState() {
        userNameErrorLiveData.value = ""
        passwordErrorLiveData.value = ""
        rePasswordErrorLiveData.value = ""
    }

    fun toRegister() {
        accountNavigator?.toRegister()
    }

    fun register() {
        if (verifyAccount(true)) {
            observerRespData(accountRepo.register(userName, password, rePassword), true, {
                accountNavigator?.loginSuccess(it.userName.orEmpty())
            })
        }
    }

    private fun verifyAccount(register: Boolean): Boolean {
        resetErrorState()
        return when {
            userName.isEmpty() -> {
                accountNavigator?.verifyUserNameError()
                false
            }
            password.length < 6 -> {
                accountNavigator?.verifyPasswordError()
                false
            }
            register and (password != rePassword) -> {
                accountNavigator?.verifyRePasswordError()
                false
            }
            else -> true
        }
    }


    interface AccountNavigator : com.aqrlei.app.open.todowanandroid.base.BaseViewModel.CommonNavigator {
        fun toRegister()
        fun loginSuccess(userName: String)
        fun verifyUserNameError()
        fun verifyPasswordError()
        fun verifyRePasswordError()
    }
}