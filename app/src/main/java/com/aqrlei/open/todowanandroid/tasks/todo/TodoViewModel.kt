package com.aqrlei.open.todowanandroid.tasks.todo

import android.app.Application
import com.aqrlei.open.todowanandroid.base.BaseViewModel
import com.aqrlei.open.todowanandroid.net.repository.TodoRepository
import com.aqrlei.open.todowanandroid.net.req.TodoReqBean

/**
 * @author aqrlei on 2018/12/28
 */

class TodoViewModel(application: Application) : BaseViewModel(application) {


    private val todoRepo = TodoRepository()



    fun fetchTypeList(type: String) {
        observerRespData(todoRepo.fetchTypeList(type), true, {

        })
    }

    fun fetchDoneList(type: String, pageNum: String) {
        observerRespData(todoRepo.fetchDoneList(type, pageNum), true, {

        })
    }

    fun fetchNotDoList(type: String, pageNum: String) {
        observerRespData(todoRepo.fetchNotDoList(type, pageNum), true, {})
    }

    fun updateStatus(id: String, status: String) {
        observerRespData(todoRepo.updateStatus(id, status), true, {})
    }

    fun updateContent(id: String, data: TodoReqBean) {
        observerRespData(todoRepo.updateContent(id, data), true, {})
    }

    fun delete(id: String) {
        observerRespData(todoRepo.delete(id), true, {})
    }

    fun addNew(data: TodoReqBean) {
        observerRespData(todoRepo.addNew(data), true, {})
    }
}