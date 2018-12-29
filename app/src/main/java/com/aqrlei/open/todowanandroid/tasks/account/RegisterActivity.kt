package com.aqrlei.open.todowanandroid.tasks.account

import android.content.Context
import android.content.Intent
import androidx.lifecycle.ViewModelProviders
import com.aqrlei.open.todowanandroid.R
import com.aqrlei.open.todowanandroid.base.ViewModelActivity
import com.aqrlei.open.todowanandroid.databinding.ActRegisterBinding
import com.aqrlei.open.utils.IntentUtil

/**
 * @author aqrlei on 2018/12/29
 */

class RegisterActivity : ViewModelActivity<AccountViewModel, ActRegisterBinding>() {
    companion object {
        fun start(context: Context) {
            val intent = Intent(context, RegisterActivity::class.java)
            if (IntentUtil.queryActivities(context, intent)) {
                context.startActivity(intent)
            }
        }
    }

    override val viewModel: AccountViewModel
        get() = ViewModelProviders.of(this).get(AccountViewModel::class.java)

    override fun bindLayout(): Int = R.layout.act_register

    override fun initComponents(binding: ActRegisterBinding) {
        binding.viewModel = viewModel
        bindTitleToolbar(binding.titleToolBar)
    }

}