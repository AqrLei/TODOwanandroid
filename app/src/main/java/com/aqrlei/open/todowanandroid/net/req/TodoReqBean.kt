package com.aqrlei.open.todowanandroid.net.req

/**
 * @author aqrlei on 2018/12/24
 */
data class TodoReqBean(
    var id: String? = null,
    var title: String,
    var content: String,
    var date: String,
    var type: String,
    var status: String? = null
)