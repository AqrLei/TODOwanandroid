package com.aqrlei.open.todowanandroid.tasks.main

import android.app.Application
import androidx.databinding.ObservableArrayList
import androidx.databinding.ObservableBoolean
import androidx.databinding.ObservableInt
import com.aqrlei.open.todowanandroid.base.BaseViewModel
import com.aqrlei.open.todowanandroid.net.repository.TodoRepository
import com.aqrlei.open.todowanandroid.net.req.TodoReqBean
import com.aqrlei.open.todowanandroid.net.resp.todo.TodoRespBean
import kotlin.properties.Delegates

/**
 * @author aqrlei on 2018/12/28
 */

class TodoViewModel(application: Application) : BaseViewModel(application) {


    var itemChoicePos: Int = 0

    val tabTitles = ObservableArrayList<String>()
    val contentList = ObservableArrayList<TodoRespBean>()
    val refreshing = ObservableBoolean()
    val itemLevel = ObservableInt()
    private val todoNavigator: TodoNavigator?
        get() = navigator as? TodoNavigator

    private val todoRepo = TodoRepository()

    private var type: Int by Delegates.observable(0) { _, oldValue, newValue ->
        if (newValue != oldValue) {
            refreshAction.invoke()
        }
    }
    private var state: Int by Delegates.observable(0) { _, oldValue, newValue ->
        if (newValue != oldValue) {
            refreshAction.invoke()
        }
    }

    val typeChangeAction = { typePosition: Int ->
        type = typePosition
    }
    val refreshAction = {
        refreshing.set(true)
        fetchList()
    }
    val typeStateChangeAction = { statePosition: Int ->
        state = statePosition
    }

    fun addNew() {
        todoNavigator?.addNew()
    }

    fun init() {
        tabTitles.clear()
        tabTitles.addAll(
            listOf(
                "未完成",
                "已完成"
            )
        )
        itemLevel.set(0)
        refreshing.set(true)
        fetchList()
    }

    fun itemClick(id: String?) {
        todoNavigator?.modifyItem(id.orEmpty())
    }

    fun itemLongClick(id: String?): Boolean {
        return todoNavigator?.manageItem(id.orEmpty()) ?: false
    }

    private fun fetchList() {
        if (state != 0) {
            itemChoicePos = 1
            fetchDoneList(type.toString())
        } else {
            itemChoicePos = 0
            fetchNotDoList(type.toString())
        }
    }

    private fun fetchDoneList(type: String, pageNum: String = "0") {
        observerRespData(todoRepo.fetchDoneList(type, pageNum), false, {
            it.datas?.run {
                refreshingFinish(this)
                itemLevel.set(1)
            }
        })
    }

    private fun fetchNotDoList(type: String, pageNum: String = "0") {
        observerRespData(todoRepo.fetchNotDoList(type, pageNum), false, {
            it.datas?.run {
                itemLevel.set(0)
                refreshingFinish(this)
            }
        })
    }

    private fun refreshingFinish(data: List<TodoRespBean>) {
        contentList.clear()
        contentList.addAll(data)
        refreshing.set(false)
    }

    fun updateStatus(id: String, status: String) {
        observerRespData(todoRepo.updateStatus(id, status), true, {
            refreshAction.invoke()
        })
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

    @Deprecated("")
    fun fetchTypeList(type: String) {
        observerRespData(todoRepo.fetchTypeList(type), true, {

        })
    }

    interface TodoNavigator : CommonNavigator {
        fun addNew()
        fun modifyItem(id: String)
        fun manageItem(id: String): Boolean
        override fun back() {

        }
    }
}