package com.aqrlei.app.open.todowanandroid.net

import okhttp3.Interceptor
import okhttp3.Response

/**
 * @author aqrlei on 2018/12/25
 */
class HttpResponseInterceptor : Interceptor {
    companion object {
        private const val USER_LOGIN_KEY = "user/login"
        private const val USER_REGISTER_KEY = "user/register"
        private const val SET_COOKIE_KEY = "Set-Cookie"
    }

    override fun intercept(chain: Interceptor.Chain): Response {
        val request = chain.request()
        val response = chain.proceed(request)
        val requestUrl = request.url().toString()
        if ((requestUrl.contains(USER_LOGIN_KEY) || requestUrl.contains(USER_REGISTER_KEY))
            && response.headers(SET_COOKIE_KEY).isNotEmpty()
        ) {
            val cookies = response.headers(SET_COOKIE_KEY).toString()
            CookieStore.putCookie(cookies)
        }
        return response
    }
}