package com.aqrlei.open.todowanandroid.tasks.account

import android.app.Application
import com.aqrlei.open.todowanandroid.base.BaseViewModel
import com.aqrlei.open.todowanandroid.net.repository.AccountRepository

/**
 * @author aqrlei on 2018/12/25
 */
class LoginViewModel(application: Application) : BaseViewModel(application) {

    private val accountRepo = AccountRepository()
    fun login(userName: String = "", password: String = "") =
        accountRepo.login(userName, password)
}