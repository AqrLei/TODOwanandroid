package com.aqrlei.app.open.todowanandroid.tasks.account


import android.content.Context
import android.content.Intent
import androidx.lifecycle.ViewModelProviders
import com.aqrlei.app.open.todowanandroid.R
import com.aqrlei.app.open.todowanandroid.databinding.ActLoginBinding
import com.aqrlei.app.open.todowanandroid.tasks.main.MainActivity
import com.aqrlei.open.utils.IntentUtil

/**
 * @author aqrlei on 2018/12/25
 */
class LoginActivity : com.aqrlei.app.open.todowanandroid.base.ViewModelActivity<AccountViewModel, ActLoginBinding>() {

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

    open inner class Navigator : CommonNavigator(), AccountViewModel.AccountNavigator {

        override fun verifyUserNameError() {
            viewModel.userNameErrorLiveData.value =
                this@LoginActivity.resources.getString(R.string.accountUserNameError)
        }

        override fun verifyPasswordError() {
            viewModel.passwordErrorLiveData.value =
                this@LoginActivity.resources.getString(R.string.accountPasswordError)
        }

        override fun verifyRePasswordError() {
            viewModel.rePasswordErrorLiveData.value =
                this@LoginActivity.resources.getString(R.string.accountRePasswordError)
        }

        override fun toRegister() {
            RegisterActivity.start(this@LoginActivity)
        }

        override fun loginSuccess(userName: String) {
            MainActivity.start(this@LoginActivity, userName)
            this@LoginActivity.finish()
        }
    }
}