package com.aqrlei.open.todowanandroid

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.aqrlei.open.todowanandroid.tasks.account.LoginActivity

class LaunchActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        LoginActivity.start(this)
    }
}
