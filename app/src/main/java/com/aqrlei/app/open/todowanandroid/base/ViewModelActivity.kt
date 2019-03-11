package com.aqrlei.app.open.todowanandroid.base

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import androidx.lifecycle.Observer
import com.aqrlei.app.open.todowanandroid.R
import com.aqrlei.open.utils.ActivityCollector
import com.aqrlei.open.utils.ToastHelper

/**
 * @author aqrlei on 2018/12/24
 */
abstract class ViewModelActivity<VM : com.aqrlei.app.open.todowanandroid.base.BaseViewModel, VB : ViewDataBinding> : AppCompatActivity(),
                                                                                                                     com.aqrlei.app.open.todowanandroid.base.BaseView {
    protected abstract val viewModel: VM

    private lateinit var binding: VB

    private var showCount: Int = 0

    private val loadingView by lazy { LayoutInflater.from(this).inflate(R.layout.progress_bar_loading, null) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(bindLayout())
        binding = DataBindingUtil.setContentView(this, bindLayout())
        binding.lifecycleOwner = this
        window.decorView.findViewById<ViewGroup>(android.R.id.content).addView(loadingView)
        ActivityCollector.add(this)
        observerData()
        initComponents(binding)
    }

    abstract fun bindLayout(): Int

    abstract fun initComponents(binding: VB)

    private fun observerData() {
        viewModel.run {
            toast.observe(this@ViewModelActivity, Observer(::showToast))
            isLoading.observe(this@ViewModelActivity, Observer(::changeLoadingState))
            navigator = CommonNavigator()
        }
    }

    protected fun bindTitleToolbar(toolbar: Toolbar,showBack:Boolean = true) {
        setSupportActionBar(toolbar)
        supportActionBar?.run {
            setDisplayHomeAsUpEnabled(showBack)
            setHomeButtonEnabled(showBack)
        }
        toolbar.setNavigationOnClickListener { viewModel.back() }
    }

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
        showCount--
        if (showCount == 0) {
            viewModel.isLoading.value = false
        }
    }

    override fun showLoading() {
        showCount++
        if (viewModel.isLoading.value != true) {
            viewModel.isLoading.value = true
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        ActivityCollector.remove(this)
    }

    open inner class CommonNavigator : com.aqrlei.app.open.todowanandroid.base.BaseViewModel.CommonNavigator {
        override fun back() {
            this@ViewModelActivity.finish()
        }
    }
}