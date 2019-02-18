package com.aqrlei.app.open.todowanandroid.tasks.main

import android.app.Application
import androidx.lifecycle.MutableLiveData
import com.aqrlei.app.open.todowanandroid.net.repository.TodoRepository
import com.aqrlei.app.open.todowanandroid.net.req.TodoReqBean

/**
 * @author aqrlei on 2018/12/28
 */

class ModifyViewModel(application: Application) : com.aqrlei.app.open.todowanandroid.base.BaseViewModel(application) {


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
            modifyNavigator?.modifyDone()
        })
    }

    fun add() {
        val data = item.value ?: TodoReqBean()
        observerRespData(todoRepo.addNew(data), true, {
            modifyNavigator?.modifyDone()
        })
    }

    interface ModifyNavigator : CommonNavigator {
        fun save()
        fun markDate()
        fun modifyDone()
    }
}