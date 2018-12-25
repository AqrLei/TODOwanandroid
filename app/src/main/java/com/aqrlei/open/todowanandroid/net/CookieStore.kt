package com.aqrlei.open.todowanandroid.net

import com.aqrlei.open.utils.AppCache
import okhttp3.Cookie

/**
 * @author aqrlei on 2018/12/25
 */
object CookieStore {
    private const val JSESSIONID = "JSESSIONID"
    private const val LOGIN_USER_NAME = "loginUserName"
    private const val TOKEN_PASS = "token_pass"
    private const val DOMAIN = "domain"

    private val cookieCache = AppCache.get()
    fun putCookie(cookies: MutableList<Cookie>) {
        cookies.filter { it.name() == JSESSIONID || it.name() == LOGIN_USER_NAME || it.name() == TOKEN_PASS }
            .forEach {
                cookieCache.putString(it.name(), "${it.name()}=${it.value()}")
                cookieCache.putString(DOMAIN, it.domain())
            }
    }

    fun getCookie(): MutableList<Cookie> {


        return ArrayList<Cookie>().apply {
            val domain = cookieCache.getString(DOMAIN, "")
            if (domain.isNotEmpty()) {
                add(
                    Cookie.Builder()
                        .name(JSESSIONID)
                        .value(cookieCache.getString(JSESSIONID, ""))
                        .domain(domain)
                        .build()
                )
                add(
                    Cookie.Builder()
                        .name(LOGIN_USER_NAME)
                        .value(cookieCache.getString(LOGIN_USER_NAME, ""))
                        .domain(domain)
                        .build()
                )
                add(
                    Cookie.Builder()
                        .name(TOKEN_PASS)
                        .value(cookieCache.getString(TOKEN_PASS, ""))
                        .domain(domain)
                        .build()
                )
            }
        }
    }

    fun clearCookie() {
        cookieCache.putString(LOGIN_USER_NAME, "")
        cookieCache.putString(TOKEN_PASS, "")
        cookieCache.putString(DOMAIN, "")
        cookieCache.putString(JSESSIONID, "")
    }
}