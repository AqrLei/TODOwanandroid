package com.aqrlei.open.todowanandroid.net.repository

import com.aqrlei.open.retrofit.livedatacalladapter.LiveObservable
import com.aqrlei.open.retrofit.livedatacalladapter.LiveResponse
import com.aqrlei.open.todowanandroid.net.NetHelper
import com.aqrlei.open.todowanandroid.net.resp.BaseRespBean
import com.aqrlei.open.todowanandroid.net.resp.account.AccountRespBean
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.GET
import retrofit2.http.POST

/**
 * @author aqrlei on 2018/12/25
 */
class AccountRepository {
    private val accountService = NetHelper.get().createService(AccountService::class.java)
    fun login(userName: String, password: String) = accountService.login(userName, password)

    fun register(userName: String, password: String, rePassword: String) =
        accountService.register(userName, password, rePassword)

    fun logout() = accountService.logout()

    interface AccountService {
        @FormUrlEncoded
        @POST("user/login")
        fun login(
            @Field("username") userName: String,
            @Field("password") password: String
        ): LiveObservable<LiveResponse<BaseRespBean<AccountRespBean>>>

        @FormUrlEncoded
        @POST("user/register")
        fun register(
            @Field("username") userName: String,
            @Field("password") password: String,
            @Field("repassword") rePassword: String
        ): LiveObservable<LiveResponse<BaseRespBean<AccountRespBean>>>

        @GET("user/logout/json")
        fun logout(): LiveObservable<LiveResponse<BaseRespBean<Any>>>
    }
}