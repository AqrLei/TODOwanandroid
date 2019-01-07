package com.aqrlei.open.todowanandroid.tasks.todo

import android.content.Context
import android.content.Intent
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProviders
import com.aqrlei.open.todowanandroid.CacheConst
import com.aqrlei.open.todowanandroid.R
import com.aqrlei.open.todowanandroid.base.ViewModelActivity
import com.aqrlei.open.todowanandroid.databinding.ActMainBinding
import com.aqrlei.open.utils.IntentUtil

/**
 * @author aqrlei on 2019/1/2
 */
class MainActivity : ViewModelActivity<MainViewModel, ActMainBinding>() {
    companion object {
        const val FRAGMENT_ME_TAG = "me"
        const val FRAGMENT_TODO_TAG = "todo"
        fun start(context: Context, userName: String) {
            val intent = Intent(context, MainActivity::class.java)
                .putExtra(CacheConst.USER_NAME_KEY, userName)
            if (IntentUtil.queryActivities(context, intent)) {
                context.startActivity(intent)
            }
        }
    }

    private var meFragment: MeFragment? = null
    private var todoFragment: TodoFragment? = null


    override val viewModel: MainViewModel
        get() = ViewModelProviders.of(this).get(MainViewModel::class.java)

    override fun bindLayout(): Int = R.layout.act_main

    override fun initComponents(binding: ActMainBinding) {
        viewModel.navigator = Navigator()
        binding.viewModel = viewModel
        meFragment = supportFragmentManager.findFragmentByTag(FRAGMENT_ME_TAG) as? MeFragment ?:
                MeFragment.newInstance()
        todoFragment = supportFragmentManager.findFragmentByTag(FRAGMENT_TODO_TAG) as? TodoFragment ?:
                TodoFragment.newInstance()
        viewModel.mainNavigator?.gotoTodoPage()
    }

    inner class Navigator : CommonNavigator(), MainViewModel.MainNavigator {

        private fun changeFragment(showFragment: Fragment?, hideFragment: Fragment?, tag: String) {
            this@MainActivity.supportFragmentManager.beginTransaction().run {
                showFragment?.also {
                    if (!it.isAdded) {
                        this.add(it, tag)
                    }
                    this.show(it)
                }
                hideFragment?.also {
                    if (it.isAdded) {
                        this.hide(it)
                    }
                }
                commit()
            }
        }

        override fun gotoMePage() {
            changeFragment(meFragment, todoFragment, FRAGMENT_ME_TAG)
        }

        override fun gotoTodoPage() {
            changeFragment(todoFragment, meFragment, FRAGMENT_TODO_TAG)
        }
    }
}
