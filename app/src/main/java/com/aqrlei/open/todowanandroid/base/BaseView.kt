package com.aqrlei.open.todowanandroid.base

/**
 * @author aqrlei on 2018/12/24
 */
interface BaseView {
    fun showToast(msg: String)
    fun showLoading()
    fun dismissLoading()
    fun changeLoadingState(isNeedLoadingShow: Boolean)
}