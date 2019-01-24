package com.aqrlei.open.todowanandroid.tasks.main

import android.app.Application
import androidx.databinding.ObservableArrayList
import androidx.lifecycle.MutableLiveData
import com.aqrlei.open.todowanandroid.base.BaseViewModel
import com.aqrlei.open.todowanandroid.net.repository.TodoRepository
import com.aqrlei.open.todowanandroid.net.req.TodoReqBean
import kotlin.properties.Delegates

/**
 * @author aqrlei on 2018/12/28
 */

class TodoViewModel(application: Application) : BaseViewModel(application) {


    val tabTitles = ObservableArrayList<String>()
    val contentList = ObservableArrayList<String>()

    val refreshing = MutableLiveData<Boolean>()

    private val todoNavigator: TodoNavigator?
        get() = navigator as? TodoNavigator

    private val todoRepo = TodoRepository()


    private var type: Int by Delegates.observable(0) { _, oldValue, newValue ->
        if (newValue != oldValue) {
            fetchList()
        }
    }
    private var state: Int by Delegates.observable(0) { _, oldValue, newValue ->
        if (newValue != oldValue) {
            fetchList()
        }
    }

    val typeChangeAction = { typePosition: Int ->
        type = typePosition
    }
    val typeStateChangeAction = { statePosition: Int ->
        state = statePosition
    }

    val refresh = {

    }

    fun addNew() {
        todoNavigator?.addNew()
    }

    fun init() {
        tabTitles.clear()
        tabTitles.addAll(
            listOf(
                "未完成",
                "已完成"))
        contentList.clear()
        contentList.addAll(
            listOf(
                "不限",
                "已完成",
                "未完成")
        )
        fetchList()
    }

    fun addContent(position: Int) {
        contentList.add(0, "test")
        // contentList.add("test")
    }


    private fun fetchList() {
        if (state != 0) {
            fetchDoneList(type.toString())
        } else {
            fetchNotDoList(type.toString())
        }
    }


    private fun fetchDoneList(type: String, pageNum: String = "0") {
        observerRespData(todoRepo.fetchDoneList(type, pageNum), true, {
            val temp = it
        })
    }

    private fun fetchNotDoList(type: String, pageNum: String = "0") {
        observerRespData(todoRepo.fetchNotDoList(type, pageNum), true, {
            val temp = it
        })
    }


    fun fetchTypeList(type: String) {
        observerRespData(todoRepo.fetchTypeList(type), true, {

        })
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