package com.aqrlei.open.todowanandroid.net.resp.todo

/**
 * @author aqrlei on 2018/12/25
 */
data class TodoRespBean(
    val id: String?,
    val userId: String?,
    val type: String?,
    val status: String?,
    val title: String?,
    val content: String?,
    val date: String?,
    val dateStr: String?,
    val completeDate: String?,
    val completeDateStr: String?
)