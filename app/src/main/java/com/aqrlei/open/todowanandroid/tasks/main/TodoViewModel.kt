package com.aqrlei.open.todowanandroid.tasks.main

import android.app.Application
import androidx.databinding.ObservableArrayList
import com.aqrlei.open.todowanandroid.base.BaseViewModel
import com.aqrlei.open.todowanandroid.net.repository.TodoRepository
import com.aqrlei.open.todowanandroid.net.req.TodoReqBean

/**
 * @author aqrlei on 2018/12/28
 */

class TodoViewModel(application: Application) : BaseViewModel(application) {


    val tabTitles = ObservableArrayList<String>()
    val contentList = ObservableArrayList<String>()

    private val todoNavigator: TodoNavigator?
        get() = navigator as? TodoNavigator

    private val todoRepo = TodoRepository()


    fun addNew() {
        todoNavigator?.addNew()
    }

    fun initTab() {
        tabTitles.clear()
        tabTitles.addAll(
            listOf(
                "不限",
                "已完成",
                "未完成"))
        contentList.clear()
        contentList.addAll(
            listOf(
                "不限",
                "已完成",
                "未完成")
        )
    }

    fun addContent() {
        contentList.add("test")
    }


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

    interface TodoNavigator : CommonNavigator {
        fun addNew()
        override fun back() {

        }
    }
}