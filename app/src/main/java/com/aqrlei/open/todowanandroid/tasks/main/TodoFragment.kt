package com.aqrlei.open.todowanandroid.tasks.main

import androidx.lifecycle.ViewModelProviders
import com.aqrlei.open.todowanandroid.R
import com.aqrlei.open.todowanandroid.base.ViewModelFragment
import com.aqrlei.open.todowanandroid.databinding.FragTodoBinding

/**
 * @author aqrlei on 2019/1/7
 */
class TodoFragment : ViewModelFragment<TodoViewModel, FragTodoBinding>() {

    companion object {
        fun newInstance() = TodoFragment()
    }

    override val viewModel: TodoViewModel
        get() = ViewModelProviders.of(this).get(TodoViewModel::class.java)

    override fun bindLayout(): Int = R.layout.frag_todo

    override fun initComponents(binding: FragTodoBinding) {
        viewModel.navigator = Navigator()
        binding.viewModel = viewModel
        viewModel.initTab()
    }

    inner class Navigator : TodoViewModel.TodoNavigator {
        override fun addNew() {
            //TODO
        }
    }
}