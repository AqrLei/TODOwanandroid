package com.aqrlei.app.open.todowanandroid.net.resp.todo

/**
 * @author aqrlei on 2018/12/25
 */
data class TodoTypeRespBean(
    val doneList: MutableList<TodoListRespBean>?,
    val todoList: MutableList<TodoListRespBean>?,
    val type: String?
)