package com.aqrlei.app.open.todowanandroid.tasks.main

import android.app.Application

/**
 * @author aqrlei on 2019/1/7
 */
class MainViewModel(application: Application) : com.aqrlei.app.open.todowanandroid.base.BaseViewModel(application) {

    private val mainNavigator: MainNavigator?
        get() = navigator as? MainNavigator
    val bottomNavigatorAction = { position: Int ->
        setSelected(position)
    }

    fun setSelected(position: Int) {
        if (position == 1) {
            mainNavigator?.gotoMePage()
        } else {
            mainNavigator?.gotoTodoPage()
        }
    }

    interface MainNavigator : CommonNavigator {
        fun gotoTodoPage()
        fun gotoMePage()
    }
}