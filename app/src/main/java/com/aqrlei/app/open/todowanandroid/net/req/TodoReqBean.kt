package com.aqrlei.app.open.todowanandroid.net.req

/**
 * @author aqrlei on 2018/12/24
 */
data class TodoReqBean(
    var id: String = "",
    var title: String = "",
    var content: String = "",
    var date: String = "",
    var dateStr: String = "",
    var type: String = "",
    var status: String = "0",
    var priority: String = ""
)