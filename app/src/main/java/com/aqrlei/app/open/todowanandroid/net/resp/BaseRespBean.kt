package com.aqrlei.app.open.todowanandroid.net.resp

/**
 * @author aqrlei on 2018/12/21
 */
data class BaseRespBean<T>(
    var errorCode: String,// -1001:未登录
    var errorMsg: String,
    var data: T?
)