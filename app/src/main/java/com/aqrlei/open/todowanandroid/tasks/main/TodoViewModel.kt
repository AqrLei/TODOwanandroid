package com.aqrlei.open.todowanandroid.tasks.main

import android.app.Application
import androidx.databinding.ObservableArrayList
import androidx.databinding.ObservableBoolean
import androidx.databinding.ObservableInt
import androidx.paging.PagedList
import androidx.recyclerview.widget.DiffUtil
import com.aqrlei.open.todowanandroid.base.BaseViewModel
import com.aqrlei.open.todowanandroid.net.repository.TodoRepository
import com.aqrlei.open.todowanandroid.net.resp.todo.TodoRespBean
import kotlin.properties.Delegates

/**
 * @author aqrlei on 2018/12/28
 */

class TodoViewModel(application: Application) : BaseViewModel(application) {

    val pageConfig = PagedList.Config.Builder()
        .setPageSize(10)
        .setEnablePlaceholders(false)
        .build()
    val loadDataAction = { _: Int, nextPage: Int ->
        fetchList(nextPage.toString())
    }
    val diffCallback = object : DiffUtil.ItemCallback<TodoRespBean>() {
        override fun areContentsTheSame(oldItem: TodoRespBean, newItem: TodoRespBean): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areItemsTheSame(oldItem: TodoRespBean, newItem: TodoRespBean): Boolean {
            return oldItem == newItem
        }
    }
    var noMoreData: Boolean = false

    var itemChoicePos: Int = 0

    val tabTitles = ObservableArrayList<String>()
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

    val typeChangeAction = { typePosition: Int ->
        type = typePosition
    }
    val refreshAction = {
        refreshing.set(true)
        noMoreData = false
        fetchList()
        Unit
    }
    val typeStateChangeAction = { statePosition: Int ->
        state = statePosition
    }

    fun addNew() {
        todoNavigator?.addNew(type.toString())
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
        refreshAction.invoke()
    }

    fun itemClick(item: TodoRespBean?) {
        todoNavigator?.modifyItem(item)
    }

    fun itemLongClick(id: String?): Boolean {
        return todoNavigator?.manageItem(id.orEmpty()) ?: false
    }

    private fun fetchList(pageNum: String = "0"): List<TodoRespBean> {
        return if (state != 0) {
            itemChoicePos = 1
            fetchDoneList(type.toString(), pageNum)
        } else {
            itemChoicePos = 0
            fetchNotDoList(type.toString(), pageNum)
        }
    }

    private fun fetchDoneList(type: String, pageNum: String = "0"): List<TodoRespBean> {

        val tempList: MutableList<TodoRespBean> = ArrayList()
        return if (noMoreData) {
            tempList
        } else {
            observerRespData(todoRepo.fetchDoneList(type, pageNum), false, {
                noMoreData = it.curPage == (it.total ?: 0 - 1)
                it.datas?.run {
                    refreshingFinish()
                    tempList.addAll(this)
                    itemLevel.set(1)
                }
            })
            Thread.sleep(1000)
            tempList
        }
    }

    private fun fetchNotDoList(type: String, pageNum: String = "0"): MutableList<TodoRespBean> {
        val tempList: MutableList<TodoRespBean> = ArrayList()
        return if (noMoreData) {
            tempList
        } else {
            observerRespData(todoRepo.fetchNotDoList(type, pageNum), false, {
                noMoreData = it.curPage == (it.total ?: 0 - 1)
                it.datas?.run {
                    itemLevel.set(0)
                    refreshingFinish()
                    tempList.addAll(this)
                }
            })
            Thread.sleep(1000)
            tempList
        }
    }

    private fun refreshingFinish() {
        refreshing.set(false)
    }

    fun updateStatus(id: String, status: String) {
        observerRespData(todoRepo.updateStatus(id, status), true, {
            refreshAction.invoke()
        })
    }

    fun delete(id: String) {
        observerRespData(todoRepo.delete(id), true, {}, successWithoutErrorAction = {
            fetchList()
        })
    }

    interface TodoNavigator : CommonNavigator {
        fun addNew(type: String)
        fun modifyItem(item: TodoRespBean?)
        fun manageItem(id: String): Boolean
        override fun back() {}
    }
}