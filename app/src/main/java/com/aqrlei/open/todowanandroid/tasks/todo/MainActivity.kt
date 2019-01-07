package com.aqrlei.open.todowanandroid.tasks.todo

import android.content.Context
import android.content.Intent
import androidx.lifecycle.ViewModelProviders
import com.aqrlei.open.todowanandroid.CacheConst
import com.aqrlei.open.todowanandroid.R
import com.aqrlei.open.todowanandroid.base.ViewModelActivity
import com.aqrlei.open.todowanandroid.databinding.ActTodoBinding
import com.aqrlei.open.utils.IntentUtil

/**
 * @author aqrlei on 2019/1/2
 */
class MainActivity : ViewModelActivity<TodoViewModel, ActTodoBinding>() {
    companion object {
        fun start(context: Context, userName: String) {
            val intent = Intent(context, MainActivity::class.java)
                .putExtra(CacheConst.USER_NAME_KEY, userName)
            if (IntentUtil.queryActivities(context, intent)) {
                context.startActivity(intent)
            }
        }
    }

    override val viewModel: TodoViewModel
        get() = ViewModelProviders.of(this).get(TodoViewModel::class.java)

    override fun bindLayout(): Int = R.layout.act_main

    override fun initComponents(binding: ActTodoBinding) {
        binding.viewModel = viewModel
    }
}
