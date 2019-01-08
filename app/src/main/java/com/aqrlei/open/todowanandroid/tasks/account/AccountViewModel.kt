package com.aqrlei.open.todowanandroid.tasks.account

import android.app.Application
import androidx.lifecycle.MutableLiveData
import com.aqrlei.open.todowanandroid.CacheConstant
import com.aqrlei.open.todowanandroid.base.BaseViewModel
import com.aqrlei.open.todowanandroid.net.repository.AccountRepository
import com.aqrlei.open.utils.ActivityCollector
import com.aqrlei.open.utils.AppCache

/**
 * @author aqrlei on 2018/12/25
 */
class AccountViewModel(application: Application) :
    BaseViewModel(application) {

    private val accountRepo = AccountRepository()
    val userNameLiveData = MutableLiveData<String>()
    val passwordLiveData = MutableLiveData<String>()
    val rePasswordLiveData = MutableLiveData<String>()

    val userNameErrorLiveData = MutableLiveData<String>()
    val passwordErrorLiveData = MutableLiveData<String>()
    val rePasswordErrorLiveData = MutableLiveData<String>()


    private val userName: String
        get() = userNameLiveData.value.orEmpty()
    private val password: String
        get() = passwordLiveData.value.orEmpty()
    private val rePassword: String
        get() = rePasswordLiveData.value.orEmpty()

    private val accountNavigator: AccountNavigator?
        get() = navigator as? AccountNavigator

    fun login() {
        userNameErrorLiveData.value = ""
        passwordErrorLiveData.value = ""
        if (verifyAccount(false)) {
            observerRespData(accountRepo.login(userName, password), true, {
                AppCache.get().putString(CacheConstant.USER_NAME_KEY, it.userName.orEmpty()).commit()
                accountNavigator?.loginSuccess(it.userName.orEmpty())
            })
        }
    }

    fun toRegister() {
        accountNavigator?.toRegister()
    }

    fun register() {
        if (verifyAccount(true)) {
            observerRespData(accountRepo.register(userName, password, rePassword), true, {

            })
        }
    }

    private fun verifyAccount(register: Boolean): Boolean {
        return when {
            userName.length < 6 -> {
                userNameErrorLiveData.value = "用户名不正确"
                false
            }
            password.length < 6 -> {
                passwordErrorLiveData.value = "密码不正确"
                false
            }
            register and (password != rePassword) -> {
                rePasswordErrorLiveData.value = "两次输入密码不匹配"
                false
            }
            else -> true
        }
    }

    fun logout() {
        observerRespData(accountRepo.logout(), false, {
            //TODO handle logout
            ActivityCollector.killApp()
        })
    }

    interface AccountNavigator : BaseViewModel.CommonNavigator {
        fun toRegister()

        fun loginSuccess(userName: String)
    }

}