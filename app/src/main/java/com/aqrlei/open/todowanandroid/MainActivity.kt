package com.aqrlei.open.todowanandroid

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.aqrlei.open.todowanandroid.tasks.account.LoginActivity
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        clickTv.setOnClickListener {
            LoginActivity.start(this)
        }
    }
}
