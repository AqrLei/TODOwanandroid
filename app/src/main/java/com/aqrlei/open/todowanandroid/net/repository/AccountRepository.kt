package com.aqrlei.open.todowanandroid.net.repository

import com.aqrlei.open.retrofit.livedatacalladapter.LiveObservable
import com.aqrlei.open.retrofit.livedatacalladapter.LiveResponse
import com.aqrlei.open.todowanandroid.net.NetConfigure
import com.aqrlei.open.todowanandroid.net.NetHelper
import com.aqrlei.open.todowanandroid.net.resp.BaseRespBean
import com.aqrlei.open.todowanandroid.net.resp.account.LoginRespBean
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.POST

/**
 * @author aqrlei on 2018/12/25
 */
class AccountRepository {
    private val accountService = NetHelper.get().createService(AccountService::class.java)
    fun login(userName: String, password: String) = accountService.login(userName, password)

    interface AccountService {
        @FormUrlEncoded
        @POST(NetConfigure.ACCOUNT_LOGIN)
        fun login(
            @Field("username") userName: String,
            @Field("password") password: String
        ): LiveObservable<LiveResponse<BaseRespBean<LoginRespBean>>>
    }
}