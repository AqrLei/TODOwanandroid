package com.aqrlei.open.todowanandroid.tasks.todo

import android.app.Application
import android.view.MenuItem
import com.aqrlei.open.todowanandroid.R
import com.aqrlei.open.todowanandroid.base.BaseViewModel

/**
 * @author aqrlei on 2019/1/7
 */
class MeViewModel(application: Application) : BaseViewModel(application) {

     val mainNavigator: MainNavigator?
        get() = navigator as? MainNavigator
    val bottomNavigatorAction = { item: MenuItem ->
        if (item.itemId == R.id.bottom_navigate_todo) {
            mainNavigator?.gotoTodoPage()
        } else {
            mainNavigator?.gotoMePage()
        }
    }

    interface MainNavigator : CommonNavigator {
        fun gotoTodoPage()
        fun gotoMePage()
    }
}