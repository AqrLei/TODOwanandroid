package com.aqrlei.open.todowanandroid.tasks.account


import android.content.Context
import android.content.Intent
import android.util.Log
import androidx.lifecycle.ViewModelProviders
import com.aqrlei.open.todowanandroid.R
import com.aqrlei.open.todowanandroid.base.ViewModelActivity
import com.aqrlei.open.todowanandroid.databinding.ActLoginBinding
import com.aqrlei.open.todowanandroid.net.CookieStore
import com.aqrlei.open.utils.IntentUtil
import kotlinx.android.synthetic.main.act_login.*

/**
 * @author aqrlei on 2018/12/25
 */
class LoginActivity : ViewModelActivity<LoginViewModel, ActLoginBinding>() {

    companion object {
        fun start(context: Context) {
            val intent = Intent(context, LoginActivity::class.java)
            if (IntentUtil.queryActivities(context, intent)) {
                context.startActivity(intent)
            }
        }
    }

    override val viewModel: LoginViewModel
        get() = ViewModelProviders.of(this).get(LoginViewModel::class.java)

    override fun bindLayout(): Int = R.layout.act_login

    override fun initComponents(binding: ActLoginBinding) {
        binding.viewModel = viewModel
    }

    private fun login() {
        viewModel.login().observable(this) {
            if (it.isSuccess) {
                it.response?.run {
                    if (errorCode == "0") {
                        Log.d("TodoTest", CookieStore.getCookieStr())
                        contentTv.text = data?.userName.orEmpty()
                    } else {
                        showToast(errorMsg)
                    }
                }
            }
        }
    }
}