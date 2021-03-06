package com.aqrlei.app.open.todowanandroid

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.aqrlei.app.open.todowanandroid.net.CookieStore
import com.aqrlei.app.open.todowanandroid.tasks.account.LoginActivity
import com.aqrlei.app.open.todowanandroid.tasks.main.MainActivity
import com.aqrlei.open.utils.AppCache

class LaunchActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        if (CookieStore.getCookieStr().isNotEmpty()) {
            MainActivity.start(
                this,
                AppCache.get().getString(com.aqrlei.app.open.todowanandroid.CacheConstant.USER_NAME_KEY, "")
            )
        } else {
            LoginActivity.start(this)
        }
        this.finish()
    }
}