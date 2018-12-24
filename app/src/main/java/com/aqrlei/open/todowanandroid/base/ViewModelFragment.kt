package com.aqrlei.open.todowanandroid.base

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment

/**
 * @author aqrlei on 2018/12/24
 */
abstract class ViewModelFragment<VM : BaseViewModel> : Fragment(), BaseView {

    protected abstract val viewModel: VM

    private val baseView: BaseView?
        get() = this.context as? BaseView

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(bindLayout(), container, false)
    }

    abstract fun bindLayout(): Int
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        observerData()
    }

    abstract fun observerData()

    override fun showToast(msg: String) {
        baseView?.showToast(msg)
    }

    override fun dismissLoading() {
        baseView?.dismissLoading()
    }

    override fun showLoading() {
        baseView?.showLoading()
    }
}