package com.aqrlei.open.todowanandroid.net

import okhttp3.Cookie
import okhttp3.CookieJar
import okhttp3.HttpUrl

/**
 * @author aqrlei on 2018/12/25
 */
class CookieHandler : CookieJar {
    override fun saveFromResponse(url: HttpUrl, cookies: MutableList<Cookie>) {
        CookieStore.putCookie(cookies)

    }

    override fun loadForRequest(url: HttpUrl): MutableList<Cookie> {
        return CookieStore.getCookie()
    }
}