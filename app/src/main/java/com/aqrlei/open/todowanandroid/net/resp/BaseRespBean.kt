package com.aqrlei.open.todowanandroid.net.resp

/**
 * @author aqrlei on 2018/12/21
 */
class BaseRespBean<T>(
        var errorCode: String,
        var errorMsg: String,
        var data: T?
)