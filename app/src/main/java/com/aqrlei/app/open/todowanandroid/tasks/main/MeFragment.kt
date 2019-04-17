package com.aqrlei.app.open.todowanandroid.tasks.main

import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.View
import android.webkit.WebView
import androidx.lifecycle.ViewModelProviders
import com.aqrlei.app.open.todowanandroid.R
import com.aqrlei.app.open.todowanandroid.databinding.FragMeBinding
import com.aqrlei.app.open.todowanandroid.net.CookieStore
import com.aqrlei.app.open.todowanandroid.tasks.account.LoginActivity
import com.aqrlei.app.open.todowanandroid.view.DialogUtil
import com.aqrlei.open.utils.ActivityCollector
import com.aqrlei.open.utils.IntentUtil
import com.aqrlei.open.utils.ToastHelper
import com.google.android.material.bottomsheet.BottomSheetDialog

/**
 * @author aqrlei on 2019/1/7
 */
class MeFragment : com.aqrlei.app.open.todowanandroid.base.ViewModelFragment<MeViewModel, FragMeBinding>() {

    companion object {
        fun newInstance(userName: String): MeFragment {
            return MeFragment().apply {
                arguments = Bundle().also {
                    it.putString(
                        com.aqrlei.app.open.todowanandroid.CacheConstant.USER_NAME_KEY,
                        userName
                    )
                }
            }
        }
    }

    override val viewModel: MeViewModel
        get() = ViewModelProviders.of(this).get(MeViewModel::class.java)

    override fun bindLayout(): Int = R.layout.frag_me

    override fun initComponents(binding: FragMeBinding) {
        viewModel.navigator = Navigator()
        binding.viewModel = viewModel
        viewModel.userName.value =
            arguments?.getString(com.aqrlei.app.open.todowanandroid.CacheConstant.USER_NAME_KEY).orEmpty()
    }

    inner class Navigator : MeViewModel.MeNavigator, View.OnClickListener {
        private var bottomDialog: BottomSheetDialog? = null
        override fun gotoAppMarket() {
            this@MeFragment.context?.let { IntentUtil.toMarketApp(it) }
        }

        override fun feedbackByEmail() {
            val intent = Intent().apply {
                action = Intent.ACTION_SENDTO
                data = Uri.parse(com.aqrlei.app.open.todowanandroid.CacheConstant.FEEDBACK_EMAIL)
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
            intent.data = Uri.parse(com.aqrlei.app.open.todowanandroid.CacheConstant.GIT_HUB)
            intent.action = Intent.ACTION_VIEW
            startActivity(intent)
        }

        override fun gotoAbout() {
            this@MeFragment.context?.run {
                DialogUtil.generateFullScreenDialog(this) { dialog ->
                    View.inflate(this, R.layout.dialog_about, null).apply {
                        this.findViewById<WebView>(R.id.licenseWv).loadUrl(this@run.getString(R.string.apache_license))
                        this.findViewById<View>(R.id.aboutClose).setOnClickListener {
                            dialog.dismiss()
                        }
                    }
                }.show()
            }
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

        override fun logoutToLogin() {
            this@MeFragment.context?.run { LoginActivity.start(this) }
            (this@MeFragment.context as? Activity)?.finish()
        }

        override fun onClick(v: View?) {
            bottomDialog?.dismiss()
            when (v?.id) {
                R.id.signOutTv -> {
                    CookieStore.clearCookie()
                    viewModel.logoutToLogin()
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