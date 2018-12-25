package com.aqrlei.open.todowanandroid.net.resp.account

import com.google.gson.annotations.SerializedName

/**
 * @author aqrlei on 2018/12/25
 */
data class LoginRespBean(
    var id:String?,
    @SerializedName("username")
    var userName:String?
)