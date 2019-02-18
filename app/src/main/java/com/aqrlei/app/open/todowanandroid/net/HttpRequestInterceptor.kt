package com.aqrlei.app.open.todowanandroid.net

import okhttp3.Interceptor
import okhttp3.Request
import okhttp3.Response

/**
 * @author aqrlei on 2018/12/25
 */
class HttpRequestInterceptor : Interceptor {
    companion object {
        private const val COOKIE_KEY = "Cookie"
    }

    override fun intercept(chain: Interceptor.Chain): Response {
        val oldRequest: Request = chain.request()
        val newRequestBuilder: Request.Builder = oldRequest.newBuilder()
        val newRequest: Request = newRequestBuilder
            .addHeader(COOKIE_KEY, CookieStore.getCookieStr())
            .build()
        return chain.proceed(newRequest)
    }
}