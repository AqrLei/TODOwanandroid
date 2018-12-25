package com.aqrlei.open.todowanandroid.net.resp.todo

/**
 * @author aqrlei on 2018/12/25
 */
data class TodoListRespBean(
    val date:String?,
    val todoList:MutableList<TodoRespBean>?
)