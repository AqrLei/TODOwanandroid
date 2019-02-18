package com.aqrlei.app.open.todowanandroid.net

import com.aqrlei.open.utils.AppCache

/**
 * @author aqrlei on 2018/12/25
 */
object CookieStore {
    private const val COOKIE_KEY = "Cookie"

    private val cookieCache = AppCache.get()

    fun putCookie(cookies: String) {
        cookieCache.putString(COOKIE_KEY, cookies)
            .commit()
    }

    fun getCookieStr(): String {
        return cookieCache.getString(COOKIE_KEY, "")
    }

    fun clearCookie() {
        cookieCache.putString(COOKIE_KEY, "")
            .commit()
    }
}