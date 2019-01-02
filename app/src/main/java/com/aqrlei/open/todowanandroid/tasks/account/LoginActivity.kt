package com.aqrlei.open.todowanandroid.tasks.account


import android.content.Context
import android.content.Intent
import androidx.lifecycle.ViewModelProviders
import com.aqrlei.open.todowanandroid.R
import com.aqrlei.open.todowanandroid.base.ViewModelActivity
import com.aqrlei.open.todowanandroid.databinding.ActLoginBinding
import com.aqrlei.open.todowanandroid.tasks.todo.TodoActivity
import com.aqrlei.open.utils.IntentUtil

/**
 * @author aqrlei on 2018/12/25
 */
class LoginActivity : ViewModelActivity<AccountViewModel, ActLoginBinding>() {

    companion object {
        fun start(context: Context) {
            val intent = Intent(context, LoginActivity::class.java)
            if (IntentUtil.queryActivities(context, intent)) {
                context.startActivity(intent)
            }
        }
    }

    override val viewModel: AccountViewModel
        get() = ViewModelProviders.of(this).get(AccountViewModel::class.java)

    override fun bindLayout(): Int = R.layout.act_login

    override fun initComponents(binding: ActLoginBinding) {
        viewModel.navigator = Navigator()
        binding.viewModel = viewModel
    }

    inner class Navigator : CommonNavigator(), AccountViewModel.AccountNavigator {
        override fun toRegister() {
            RegisterActivity.start(this@LoginActivity)
        }

        override fun loginSuccess() {
            TodoActivity.start(this@LoginActivity)
        }
    }
}