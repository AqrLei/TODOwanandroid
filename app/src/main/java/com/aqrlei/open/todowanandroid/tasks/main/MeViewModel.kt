package com.aqrlei.open.todowanandroid.tasks.main

import android.app.Application
import androidx.lifecycle.MutableLiveData
import com.aqrlei.open.todowanandroid.base.BaseViewModel

/**
 * @author aqrlei on 2019/1/7
 */
class MeViewModel(application: Application) : BaseViewModel(application) {

    private val meNavigator: MeNavigator?
        get() = navigator as? MeNavigator

    val userName = MutableLiveData<String>()
    fun rate() {
        meNavigator?.gotoAppMarket()
    }

    fun feedbackByEmail() {
        meNavigator?.feedbackByEmail()
    }

    fun gotoGitHub() {
        meNavigator?.gotoGitHub()
    }

    fun gotoAbout() {
        meNavigator?.gotoAbout()
    }

    fun logout() {
        meNavigator?.logout()
    }


    interface MeNavigator : CommonNavigator {
        fun gotoAppMarket()
        fun feedbackByEmail()
        fun gotoGitHub()
        fun gotoAbout()
        fun logout()
        override fun back() {

        }
    }
}