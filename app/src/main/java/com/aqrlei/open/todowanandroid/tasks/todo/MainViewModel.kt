package com.aqrlei.open.todowanandroid.tasks.todo

import android.app.Application
import android.view.MenuItem
import com.aqrlei.open.todowanandroid.R
import com.aqrlei.open.todowanandroid.base.BaseViewModel

/**
 * @author aqrlei on 2019/1/7
 */
class MainViewModel(application: Application) : BaseViewModel(application) {

    private val mainNavigator: MainNavigator?
        get() = navigator as? MainNavigator
    val bottomNavigatorAction = { item: MenuItem ->
        setSelected(item.itemId)
    }

    fun setSelected(id: Int): Boolean {
        return when (id) {
            R.id.bottom_navigate_todo -> {
                mainNavigator?.gotoTodoPage()
                true
            }
            R.id.bottom_navigate_me -> {
                mainNavigator?.gotoMePage()
                true
            }
            else -> false
        }
    }

    interface MainNavigator : CommonNavigator {
        fun gotoTodoPage()
        fun gotoMePage()
    }
}