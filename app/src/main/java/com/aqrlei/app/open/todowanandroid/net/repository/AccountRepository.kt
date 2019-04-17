package com.aqrlei.app.open.todowanandroid.net.repository

import com.aqrlei.app.open.todowanandroid.net.NetHelper
import com.aqrlei.app.open.todowanandroid.net.resp.BaseRespBean
import com.aqrlei.app.open.todowanandroid.net.resp.account.AccountRespBean
import com.aqrlei.open.retrofit.livedatacalladapter.LiveObservable
import com.aqrlei.open.retrofit.livedatacalladapter.LiveResponse
import retrofit2.http.FieldMap
import retrofit2.http.FormUrlEncoded
import retrofit2.http.GET
import retrofit2.http.POST

/**
 * @author aqrlei on 2018/12/25
 */
class AccountRepository {
    private val accountService = NetHelper.get().createService(AccountService::class.java)
    fun login(userName: String, password: String) = accountService.login(HashMap<String, String>().apply {
        put("username", userName)
        put("password", password)
    })


    fun register(userName: String, password: String, rePassword: String) =
        accountService.register(HashMap<String, String>().apply {
            put("username", userName)
            put("password", password)
            put("repassword", rePassword)
        })


    fun logout() = accountService.logout()

    interface AccountService {
        @FormUrlEncoded
        @POST("user/login")
        fun login(
            @FieldMap map: MutableMap<String, String>
        ): LiveObservable<LiveResponse<BaseRespBean<AccountRespBean>>>

        @FormUrlEncoded
        @POST("user/register")
        fun register(
            @FieldMap map: MutableMap<String, String>
        ): LiveObservable<LiveResponse<BaseRespBean<AccountRespBean>>>

        @GET("user/logout/json")
        fun logout(): LiveObservable<LiveResponse<BaseRespBean<Any>>>
    }
}