package com.aqrlei.open.todowanandroid.net.resp

/**
 * @author aqrlei on 2018/12/29
 */
data class PaginationRespBean<T>(
    var curPage: Int?,
    var offset: String?,
    var over: String?,
    var datas: MutableList<T>?,
    var pageCount: String?,
    var size: String?,
    var total: Int?
)
