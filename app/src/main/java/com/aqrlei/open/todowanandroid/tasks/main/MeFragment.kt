package com.aqrlei.open.todowanandroid.tasks.main

import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.View
import androidx.lifecycle.ViewModelProviders
import com.aqrlei.open.todowanandroid.CacheConstant
import com.aqrlei.open.todowanandroid.R
import com.aqrlei.open.todowanandroid.base.ViewModelFragment
import com.aqrlei.open.todowanandroid.databinding.FragMeBinding
import com.aqrlei.open.todowanandroid.net.CookieStore
import com.aqrlei.open.todowanandroid.tasks.account.LoginActivity
import com.aqrlei.open.todowanandroid.view.DialogUtil
import com.aqrlei.open.utils.ActivityCollector
import com.aqrlei.open.utils.IntentUtil
import com.aqrlei.open.utils.ToastHelper
import com.google.android.material.bottomsheet.BottomSheetDialog

/**
 * @author aqrlei on 2019/1/7
 */
class MeFragment : ViewModelFragment<MeViewModel, FragMeBinding>() {

    companion object {
        fun newInstance(userName: String): MeFragment {
            return MeFragment().apply {
                arguments = Bundle().also { it.putString(CacheConstant.USER_NAME_KEY, userName) }
            }
        }
    }

    override val viewModel: MeViewModel
        get() = ViewModelProviders.of(this).get(MeViewModel::class.java)

    override fun bindLayout(): Int = R.layout.frag_me

    override fun initComponents(binding: FragMeBinding) {
        viewModel.navigator = Navigator()
        binding.viewModel = viewModel
        viewModel.userName.value = arguments?.getString(CacheConstant.USER_NAME_KEY).orEmpty()
    }

    inner class Navigator : MeViewModel.MeNavigator, View.OnClickListener {
        private var bottomDialog: BottomSheetDialog? = null
        override fun gotoAppMarket() {
            this@MeFragment.context?.let { IntentUtil.toMarketApp(it) }
        }

        override fun feedbackByEmail() {
            val intent = Intent().apply {
                action = Intent.ACTION_SENDTO
                data = Uri.parse(CacheConstant.FEEDBACK_EMAIL)
                putExtra(Intent.EXTRA_SUBJECT, getString(R.string.subject))
            }
            try {
                startActivity(intent)
            } catch (e: Exception) {
                ToastHelper.getHelper().show("not found email app")
            }
        }

        override fun gotoGitHub() {
            val intent = Intent()
            intent.data = Uri.parse(CacheConstant.GIT_HUB)
            intent.action = Intent.ACTION_VIEW
            startActivity(intent)
        }

        override fun gotoAbout() {
        }

        override fun logout() {
            this@MeFragment.context?.run {
                bottomDialog = DialogUtil.generateBottomDialog(this) {
                    View.inflate(this, R.layout.dialog_bottom_sheet, null).apply {
                        this.findViewById<View>(R.id.signOutTv).setOnClickListener(this@Navigator)
                        this.findViewById<View>(R.id.killAppTv).setOnClickListener(this@Navigator)
                        this.findViewById<View>(R.id.cancelTv).setOnClickListener(this@Navigator)
                    }
                }
                bottomDialog?.show()
            }
        }

        override fun onClick(v: View?) {
            bottomDialog?.dismiss()
            when (v?.id) {
                R.id.signOutTv -> {
                    CookieStore.clearCookie()
                    this@MeFragment.context?.run { LoginActivity.start(this) }
                    (this@MeFragment.context as? Activity)?.finish()
                }
                R.id.killAppTv -> {
                    ActivityCollector.killApp()
                }
                else -> {
                }
            }
        }

    }
}