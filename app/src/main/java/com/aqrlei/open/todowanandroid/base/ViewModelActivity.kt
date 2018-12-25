package com.aqrlei.open.todowanandroid.base

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import com.aqrlei.open.retrofit.livedatacalladapter.LiveObservable
import com.aqrlei.open.retrofit.livedatacalladapter.LiveResponse
import com.aqrlei.open.todowanandroid.R
import com.aqrlei.open.todowanandroid.net.resp.BaseRespBean
import com.aqrlei.open.utils.ToastHelper

/**
 * @author aqrlei on 2018/12/24
 */
abstract class ViewModelActivity<VM : BaseViewModel> : AppCompatActivity(), BaseView {
    protected abstract val viewModel: VM

    private val loadingView by lazy { LayoutInflater.from(this).inflate(R.layout.progress_bar_loading, null) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(bindLayout())
        window.decorView.findViewById<ViewGroup>(android.R.id.content).addView(loadingView)
        initComponents()
        observerData()
    }


    abstract fun bindLayout(): Int

    abstract fun initComponents()

    private fun observerData() {
        viewModel.run {
            toast.observe(this@ViewModelActivity, Observer(::showToast))
            isLoading.observe(this@ViewModelActivity, Observer(::changeLoadingState))
        }
    }


    abstract fun <T> observerRespData()

    override fun showToast(msg: String) {
        ToastHelper.getHelper().show(msg)
    }

    private fun changeLoadingState(isNeedLoadingShow: Boolean) {
        loadingView.visibility = if (isNeedLoadingShow) {
            loadingView.setOnClickListener(null)
            View.VISIBLE
        } else {
            View.GONE
        }
    }

    override fun dismissLoading() {
        viewModel.isLoading.value = false
    }

    override fun showLoading() {
        viewModel.isLoading.value = true
    }
}