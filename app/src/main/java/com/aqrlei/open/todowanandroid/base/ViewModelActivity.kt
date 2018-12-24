package com.aqrlei.open.todowanandroid.base

import android.os.Bundle
import androidx.annotation.CallSuper
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import com.aqrlei.open.utils.ToastHelper

/**
 * @author aqrlei on 2018/12/24
 */
abstract class ViewModelActivity<VM : BaseViewModel> : AppCompatActivity(), BaseView {
    protected abstract val viewModel: VM


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(bindLayout())
        observerData()
    }


    abstract fun bindLayout(): Int

    @CallSuper
    protected fun observerData() {
        viewModel.apply {
            toast.observe(this@ViewModelActivity, Observer(::showToast))
            isLoading.observe(this@ViewModelActivity, Observer(::changeLoadingState))
        }
    }

    override fun showToast(msg: String) {
        ToastHelper.getHelper().show(msg)
    }

    override fun changeLoadingState(isNeedLoadingShow: Boolean) {
        if (isNeedLoadingShow) {
            showLoading()
        } else {
            dismissLoading()
        }
    }

    override fun dismissLoading() {
    }

    override fun showLoading() {

    }
}