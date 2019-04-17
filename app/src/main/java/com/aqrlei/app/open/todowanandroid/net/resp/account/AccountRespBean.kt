package com.aqrlei.app.open.todowanandroid.net.resp.account

import com.google.gson.annotations.SerializedName

/**
 * @author aqrlei on 2018/12/25
 */
data class AccountRespBean(
    var id: String?,
    @SerializedName("username")
    var userName: String?
)