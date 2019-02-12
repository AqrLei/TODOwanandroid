package com.aqrlei.open.todowanandroid.tasks.main

import android.app.Application
import android.util.Log
import androidx.lifecycle.MutableLiveData
import com.aqrlei.open.todowanandroid.base.BaseViewModel
import com.aqrlei.open.todowanandroid.net.repository.TodoRepository
import com.aqrlei.open.todowanandroid.net.req.TodoReqBean

/**
 * @author aqrlei on 2018/12/28
 */

class ModifyViewModel(application: Application) : BaseViewModel(application) {


    private val modifyNavigator: ModifyNavigator?
        get() = navigator as? ModifyNavigator
    private val todoRepo = TodoRepository()
    val item = MutableLiveData<TodoReqBean>()

    var editEnable: Boolean = true

    fun save() {
        modifyNavigator?.save()
    }

    fun markDate() {
        modifyNavigator?.markDate()
    }

    fun update() {
        val id = item.value?.id.orEmpty()
        val data = item.value ?: TodoReqBean()
        observerRespData(todoRepo.updateContent(id, data), true, {
            Log.d("Modify", "update")
        })
    }

    fun add() {
        val data = (item.value ?: TodoReqBean()).apply {
            id = null
            date = dateStr
            dateStr = null
            status = null
        }
        observerRespData(todoRepo.addNew(data), true, {
            Log.d("Modify", "add")
        })
    }

    interface ModifyNavigator : CommonNavigator {
        fun save()
        fun markDate()
    }
}