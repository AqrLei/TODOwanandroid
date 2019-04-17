package com.aqrlei.app.open.todowanandroid.tasks.main

import android.app.Application
import androidx.databinding.ObservableArrayList
import androidx.databinding.ObservableBoolean
import androidx.databinding.ObservableInt
import com.aqrlei.app.open.todowanandroid.net.repository.TodoRepository
import com.aqrlei.app.open.todowanandroid.net.resp.todo.TodoRespBean
import kotlin.properties.Delegates

/**
 * @author aqrlei on 2018/12/28
 */

class TodoViewModel(application: Application) : com.aqrlei.app.open.todowanandroid.base.BaseViewModel(application) {

    private var noMoreData: Boolean = false
    var itemChoicePos: Int = 0

    private var curPage: Int = 1

    val tabTitles = ObservableArrayList<String>()
    val contentList = ObservableArrayList<TodoRespBean>()
    val refreshing = ObservableBoolean()
    val itemLevel = ObservableInt()
    private val todoNavigator: TodoNavigator?
        get() = navigator as? TodoNavigator

    private val todoRepo = TodoRepository()
    var type: Int by Delegates.observable(0) { _, oldValue, newValue ->
        if (newValue != oldValue) {
            refreshAction.invoke()
        }
    }
    private var state: Int by Delegates.observable(0) { _, oldValue, newValue ->
        if (newValue != oldValue) {
            refreshAction.invoke()
        }
    }

    val refreshAction = {
        refreshing.set(true)
        noMoreData = false
        curPage = 1
        fetchList(curPage.toString())
        Unit
    }

    var loading: Boolean = false
    val loadAction = {
        if (!loading) {
            loading = true
            if (!noMoreData) {
                fetchList((++curPage).toString())
            } else {
                todoNavigator?.showNoMoreData()
            }
        }
        Unit
    }
    val typeChangeAction = { typePosition: Int ->
        type = typePosition
    }

    val typeStateChangeAction = { statePosition: Int ->
        state = statePosition
    }

    fun addNew() {
        todoNavigator?.addNew(type.toString())
    }

    fun init(titles: Array<String>) {
        tabTitles.clear()
        tabTitles.addAll(
            titles
        )
        itemLevel.set(0)
        refreshAction.invoke()
    }

    fun itemClick(item: TodoRespBean?) {
        todoNavigator?.modifyItem(item)
    }

    fun itemLongClick(id: String?): Boolean {
        return todoNavigator?.manageItem(id.orEmpty()) ?: false
    }

    private fun fetchList(pageNum: String = "0") {
        if (state != 0) {
            itemChoicePos = 1
            fetchDoneList(type.toString(), pageNum)
        } else {
            itemChoicePos = 0
            fetchNotDoList(type.toString(), pageNum)
        }
    }

    private fun fetchDoneList(
        type: String,
        pageNum: String = "0"
    ) {
        observerRespData(todoRepo.fetchDoneList(type, pageNum), !refreshing.get(), {
            noMoreData = it.curPage == (it.total ?: 0 - 1)
            it.datas?.run {
                refreshingFinish(this)
                itemLevel.set(1)
            }
        }, finishAction = {
            reqFinish()
        })
    }

    private fun fetchNotDoList(type: String, pageNum: String = "0") {
        observerRespData(todoRepo.fetchNotDoList(type, pageNum), !refreshing.get(), {
            noMoreData = it.curPage == it.pageCount
            it.datas?.run {
                itemLevel.set(0)
                refreshingFinish(this)
            }
        }, finishAction = {
            reqFinish()
        })
    }

    private fun refreshingFinish(data: List<TodoRespBean>) {
        if (refreshing.get()) {
            contentList.clear()
        }
        contentList.addAll(data)
    }

    private fun reqFinish() {
        if (refreshing.get()) {
            refreshing.set(false)
        }
        loading = false
    }

    fun updateStatus(id: String, status: String) {
        observerRespData(todoRepo.updateStatus(id, status), true, {
            refreshAction.invoke()
        })
    }

    fun delete(id: String) {
        observerRespData(todoRepo.delete(id), true, {}, successWithoutErrorAction = {
            refreshAction.invoke()
        })
    }

    interface TodoNavigator : CommonNavigator {
        fun addNew(type: String)
        fun showNoMoreData()
        fun modifyItem(item: TodoRespBean?)
        fun manageItem(id: String): Boolean
        override fun back() {}
    }
}