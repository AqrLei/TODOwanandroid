package com.aqrlei.open.todowanandroid.tasks.main

import android.app.Application
import androidx.databinding.ObservableArrayList
import androidx.databinding.ObservableBoolean
import androidx.databinding.ObservableInt
import androidx.lifecycle.MutableLiveData
import androidx.paging.PagedList
import androidx.recyclerview.widget.DiffUtil
import com.aqrlei.open.bindingadapter.adapter.datasource.SimpleDataSourceFactory
import com.aqrlei.open.todowanandroid.base.BaseViewModel
import com.aqrlei.open.todowanandroid.net.repository.TodoRepository
import com.aqrlei.open.todowanandroid.net.resp.todo.TodoRespBean
import kotlin.properties.Delegates

/**
 * @author aqrlei on 2018/12/28
 */

class TodoViewModel(application: Application) : BaseViewModel(application) {


    val pageConfig = PagedList.Config.Builder()
        .setPageSize(5)
        .setEnablePlaceholders(false)
        .build()
    val loadDataAction = { _: Int, nextPage: Int ->
        fetchList(nextPage.toString())
    }
    val dataSourceType = SimpleDataSourceFactory.DataSourceType.PAGE
    val refreshEvent = MutableLiveData<Any>()
    val currentPage = MutableLiveData<Int>().apply { value = 0 }
    val diffCallback = object : DiffUtil.ItemCallback<TodoRespBean>() {
        override fun areContentsTheSame(oldItem: TodoRespBean, newItem: TodoRespBean): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areItemsTheSame(oldItem: TodoRespBean, newItem: TodoRespBean): Boolean {
            return oldItem == newItem
        }
    }

    var itemChoicePos: Int = 0

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

    private fun fetchList(pageNum: String = "0") {
        if (state != 0) {
            itemChoicePos = 1
            fetchDoneList(type.toString(), pageNum)
        } else {
            itemChoicePos = 0
            fetchNotDoList(type.toString(), pageNum)
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

    fun delete(id: String) {
        observerRespData(todoRepo.delete(id), true, {}, successWithoutErrorAction = {
            fetchList()
        })
    }

    interface TodoNavigator : CommonNavigator {
        fun addNew(type: String)
        fun modifyItem(item: TodoRespBean?)
        fun manageItem(id: String): Boolean
        override fun back() {

        }
    }
}